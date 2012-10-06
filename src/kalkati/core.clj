(ns kalkati.core
  (:require [clojure.string :refer [split]]
            [clojure.java.io :refer [writer]]
            [clj-http.client :as http]
            [clojure.data.xml :refer :all])
  (:import [java.io PrintWriter])
  (:gen-class))

(def hsl-username (or (System/getenv "HSL_API_USERNAME") (System/getProperty "hsl.api.username")))
(def hsl-password (or (System/getenv "HSL_API_PASSWORD") (System/getProperty "hsl.api.password")))

(def cities
  {"1" "Helsinki"
   "2" "Espoo"
   "3" "Kauniainen"
   "4" "Vantaa"
   "6" "Kirkkonummi"
   "9" "Kerava"})

(defn- city-writers [args]
  (->> args
       (map
         (fn [arg]
           (let [[city filename] (split arg #"=")]
             [city (PrintWriter. (writer filename :append true) true)])))
       (into {})))

(defn- close-city-writers [writers]
  (doseq [writer (vals writers)]
    (.close writer)))

(defmacro with-open-city-writers [[writers args] & body]
  `(let [~writers (city-writers ~args)]
     (try
       ~@body
       (finally (close-city-writers ~writers)))))

(defn- kalkati-data []
  (let [in (-> "http://api.reittiopas.fi/data/all.zip"
                (http/get {:basic-auth [hsl-username hsl-password]
                           :as :stream})
                :body)]
    (doto (java.util.zip.ZipInputStream. in)
      (.getNextEntry))))

(defn- stations [stream]
  (->> stream
       parse
       :content
       (filter #(= :Station (:tag %)))
       (map :attrs)))

(defn -main [& args]
  (with-open [stream (kalkati-data)]
    (with-open-city-writers [writers args]
      (doseq [{:keys [Name X Y city_id]} (stations stream)]
        (when-let [^PrintWriter writer (writers (cities city_id))]
          (doto writer
            (.print Name)
            (.print "|")
            (.print X)
            (.print ",")
            (.print Y)
            (.println)))))))


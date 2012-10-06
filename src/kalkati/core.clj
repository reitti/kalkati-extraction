(ns kalkati.core
  (:require [clj-http.client :as http]
            [clojure.java.io :refer [reader]])
  (:gen-class))

(def hsl-username (or (System/getenv "HSL_API_USERNAME") (System/getProperty "hsl.api.username")))
(def hsl-password (or (System/getenv "HSL_API_PASSWORD") (System/getProperty "hsl.api.password")))

(defn- kalkati-data []
  (let [in (-> "http://api.reittiopas.fi/data/all.zip"
                (http/get {:basic-auth [hsl-username hsl-password]
                           :as :stream})
                :body
                java.util.zip.ZipInputStream.)]
    (.getNextEntry in)
    (reader in)))

(defn -main [& args]
  (with-open [stream (kalkati-data)]
    (doseq [line (line-seq stream)]
      (println line))))


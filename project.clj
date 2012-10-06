(defproject kalkati-extraction "0.1.0-SNAPSHOT"
  :repositories [["sonatype" "https://oss.sonatype.org/content/groups/public/"]]
  :dependencies [[org.clojure/clojure "1.5.0-master-SNAPSHOT"]
                 [clj-http "0.5.5"]
                 [org.clojure/data.xml "0.0.6"]]
  :main kalkati.core)
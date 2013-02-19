(defproject frostalert "0.1.0-SNAPSHOT"
  :description "When run, sends an email to the given email(s) if the low for that day is lower then the given amount"
  :url "https://github.com/mediocregopher/frostalert"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojars.jhowarth/clojure-contrib "1.2.0-RC3"]
                 [com.draines/postal  "1.9.0"]
                 [clj-http "0.1.3"]
                 [cheshire "5.0.1"]]
  :main frostalert.core)

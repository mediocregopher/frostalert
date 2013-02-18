(ns frostalert.core
    (:gen-class :main true)
    (:use postal.core)
    (:use clojure.contrib.command-line)
    (:require [clj-http.client :as client]
              [cheshire.core :refer :all]))

(defn format-request
    [api-key state city]
    (str "http://api.wunderground.com/api/" api-key
         "/forecast/q/" state "/" city ".json"))

(defn get-todays-low
    [api-key state city]
    (-> (format-request api-key state city)               ; - Create url
        (client/get )                                     ; - Get url
        (get-in [:body])                                  ; - Get body from return
        (parse-string true)                               ; - Parse it as json
        (get-in [:forecast :simpleforecast :forecastday]) ; - Get list of daily's
        (first)                                           ; - First in list is today
        (get-in [:low :fahrenheit])                       ; - Low in fahrenheit
        (read-string)))                                   ; - Convert to number
                                                          ;   ^^ probably unsafe, w/e

;bcpyopmbnoanjhax
(defn send-mail
    "Sends an email using a gmail account. Give it the gmail username (no @gmail.com
    or whatever) and the gmail password (if using two-factor auth make an
    application specific password and give that. to can be a list"
    [gmail-user gmail-pass from to subject body]
    (println "Sending email with\nsubject:" subject "\nbody:" body)
    (send-message ^{ :host "smtp.gmail.com"
                     :user gmail-user
                     :pass gmail-pass
                     :ssl :fer-sure }
                   { :from from
                     :to to
                     :subject subject
                     :body body }))


(defn -main [& args]
    (with-command-line args
        "Sends an email to the given email if the low for today is forecasted to be below the given threshold temperature\n\nUsage: {command} [OPTIONS] [list of emails to send to]"
        [[ api-key "wunderground.com api key" ]
         [ gmail-user "gmail username" ]
         [ gmail-pass "gmail password (if you're using two-factor make an app specific password and put that here" ]
         [ state "state to get forecast for" ]
         [ city "city to get forecast for" ]
         [ temperature "threshold temperature. if forecasted to be below, send an email" ]
         [ email-from "email the alert email should be from" ]
         email-to]

    (when (or (not api-key) (not gmail-user) (not gmail-pass) (not state) (not city)
              (not temperature) (not email-from))
        (binding [*out* *err*]          
            (println "You're missing a required argument. Try -h for help")
            (System/exit 1)))

    ;Make sure they specified people to send mail to
    (when (empty? email-to)
        (binding [*out* *err*]          
            (println "Last arguments should be email addresses to send alerts to. Try -h for help")
            (System/exit 1)))

    (try
        (if-let [fore-temp (get-todays-low api-key state city)]
            (when (> (read-string temperature) fore-temp)
                (send-mail gmail-user gmail-pass email-from email-to
                           "Oh lahd jesus it's gonna be cold!"
                           (str "Low for today is forecasted to be " fore-temp
                                ", gear up broski")))
            (send-mail gmail-user gmail-pass email-from email-to
                       "Frostalert may be broke"
                       (str "Check what happens when you do: curl "
                            (format-request api-key state city))))
    (catch Exception e
        (send-mail gmail-user gmail-pass email-from email-to
                   "Frostalert may be broke"
                   (str "Check what happens when you do: curl "
                        (format-request api-key state city)))))))

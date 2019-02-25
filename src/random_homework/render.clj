(ns random-homework.render
  (:require [clojure.string :as string]
            [random-homework.parse :as parse]))


(defn render-field
  "Formats the string value by right padding it with spaces to width 
  characters. If the length of value is longer than width then value is 
  truncated."
  [value width]
  (format (str "%-" width "s") 
          (subs value 0 (min (count value) width))))



(def header-record
  {:last-name "Last Name"
   :first-name "First Name"
   :gender "Gender"
   :fav-color "Favorite Color"
   :date-of-birth "Date of Birth"})


(defn render-record*
  [{:keys [first-name last-name gender fav-color date-of-birth]}]
  (string/join ""
               [(render-field last-name 25)
                (render-field first-name 25)
                (render-field gender 15)
                (render-field fav-color 20)
                (render-field date-of-birth 13)]))


(defn render-record
  "Given a value conformed with :person/record renders it to a single line, 
  returns the rendered value as a string"
  [rec]
  (render-record* (update rec :date-of-birth parse/format-date)))


(def header-record
  {:last-name "Last Name"
   :first-name "First Name"
   :gender "Gender"
   :fav-color "Favorite Color"
   :date-of-birth "Date of Birth"})


(defn render-records
  "Given a sequence of :person/record confomed values formats them and prints
  them to *out*"
  [recs]
  (println (render-record* header-record))
  (println)
  (doseq [r recs]
    (println (render-record r))))


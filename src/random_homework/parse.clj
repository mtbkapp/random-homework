(ns random-homework.parse
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string]
            [java-time :as jt])
  (:import [java.time.format DateTimeParseException]))


(def delims
  {"space" " "
   "comma" ","
   "pipe" "|"})


(def date-format "MM/dd/yyyy")


(defn parse-date
  "Parses the string representation in the format specified by the var 
  date-format into a java.time.LocalDate, clojure.spec.alpha/invalid otherwise."
  [date-str]
  (try
    (jt/local-date date-format date-str)
    (catch DateTimeParseException _
      :spec/invalid)))


(defn format-date 
  "Given a java.time.LocalDate instance formats it into a string with the format
  given by the date-format var."
  [date]
  (jt/format date-format date))


(defn no-delim-str?
  "Predicate the indicates if the given string does not contain the file 
  delimiters."
  [s]
  (nil? (re-find #"\||,|\s" s)))


(spec/def ::str-field (spec/and string? no-delim-str? not-empty))
(spec/def :person/first-name ::str-field)
(spec/def :person/last-name ::str-field)
(spec/def :person/gender #{"M" "F"})
(spec/def :person/fav-color ::str-field)
(spec/def :person/date-of-birth
  (spec/with-gen
    (spec/conformer parse-date format-date)
    #(gen/fmap (comp format-date
                     (fn [inst] (jt/local-date inst (jt/zone-id "UTC"))) 
                     jt/instant)
      (gen/int))))


(spec/def :person/record
  (spec/cat :last-name :person/last-name
            :first-name :person/first-name
            :gender :person/gender
            :fav-color :person/fav-color
            :date-of-birth :person/date-of-birth))


(defn delim-pattern
  "Given a delimiter returns a regex pattern that matches that delimiter."
  [delim]
  (re-pattern (if (= delim "|") "\\|" delim)))


(defn parse-line
  "Parses a line of text into data and conforms it with the :person/record spec."
  [delim line]
  (->> (string/split line (delim-pattern delim))
       (map string/trim)
       (spec/conform :person/record)))


(defn parse-file
  "Parses the given file into a vector of data which each item in the vector 
  conforms to the :person/record spec."
  [file delim]
  (with-open [rdr (io/reader file)]
    (into [] 
          (map (partial parse-line delim))
          (line-seq rdr))))


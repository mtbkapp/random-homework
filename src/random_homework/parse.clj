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
  [date-str]
  (try
    (jt/local-date date-format date-str)
    (catch DateTimeParseException _
      :spec/invalid)))


(defn format-date 
  [date]
  (jt/format date-format date))


(defn no-delim-str?
  [s]
  (nil? (re-find #"\||,|\s" s)))


(spec/def :person/first-name (spec/and string? no-delim-str? not-empty))
(spec/def :person/last-name (spec/and string? no-delim-str? not-empty))
(spec/def :person/gender #{"M" "F"})
(spec/def :person/fav-color (spec/and string? no-delim-str? not-empty))
(spec/def :person/date-of-birth
  (spec/with-gen
    (spec/conformer parse-date format-date)
    #(gen/fmap (comp format-date
                     (fn [inst] (jt/local-date inst (jt/zone-id "UTC"))) 
                     jt/instant)
      (gen/int))))


(spec/def :person/record
  (spec/cat :first-name :person/first-name
            :last-name :person/last-name
            :gender :person/gender
            :fav-color :person/fav-color
            :date-of-birth :person/date-of-birth))


(defn delim-pattern
  [delim]
  (re-pattern (if (= delim "|") "\\|" delim)))


(defn parse-line
  [delim line]
  (->> (string/split line (delim-pattern delim))
       (map string/trim)
       (spec/conform :person/record)))


(defn parse-file
  [{:keys [op/file op/delim]}]
  (with-open [rdr (io/reader file)]
    (into [] 
          (map (partial parse-line delim))
          (line-seq rdr))))

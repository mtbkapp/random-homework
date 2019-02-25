(ns random-homework.process
  (:require [java-time :as jt]))


(defn by-gender
  [{gender-a :gender lname-a :last-name} {gender-b :gender lname-b :last-name}]
  (let [gender-compare (compare gender-a gender-b)]
    (if (zero? gender-compare)
      (compare lname-a lname-b)
      gender-compare)))


(def date-of-birth-milli 
  (comp #(.toEpochDay %) :date-of-birth))


(defn by-date-of-birth
  [rec-a rec-b]
  (compare (date-of-birth-milli rec-a)
           (date-of-birth-milli rec-b)))


(defn by-last-name
  [rec-a rec-b]
  (compare (:last-name rec-b)
           (:last-name rec-a)))


(def sort-by-comparator
  {"last-name" by-last-name
   "date-of-birth" by-date-of-birth
   "gender" by-gender})


(defn sort-records
  [recs sort-by-option]
  (sort-by identity (sort-by-comparator sort-by-option) recs))

(ns random-homework.process
  (:require [java-time :as jt]))


(defn by-gender
  "A comparator to sort records by gender (females before males) then by last
  name in ascending order."
  [{gender-a :gender lname-a :last-name} {gender-b :gender lname-b :last-name}]
  (let [gender-compare (compare gender-a gender-b)]
    (if (zero? gender-compare)
      (compare lname-a lname-b)
      gender-compare)))


(def date-of-birth-milli 
  (comp #(.toEpochDay %) :date-of-birth))


(defn by-date-of-birth
  "A comparator to sort records by date of birth in ascending order."
  [rec-a rec-b]
  (compare (date-of-birth-milli rec-a)
           (date-of-birth-milli rec-b)))


(defn by-last-name
  "A comparator to sort records by last name in descending order."
  [rec-a rec-b]
  (compare (:last-name rec-b)
           (:last-name rec-a)))


(def sort-by-comparators
  {:sort-by/last-name by-last-name
   :sort-by/date-of-birth by-date-of-birth
   :sort-by/gender by-gender})


(defn sort-records
  "Given a sequence of records sorts them by the sort-by-option which is one
  of the keys in the sort-by-comparator map."
  [recs sort-by-option]
  (sort-by identity (sort-by-comparators sort-by-option) recs))


(defn combine-records
  "Given several sequences of records, combines then into a single vector."
  [& record-sets]
  (reduce into [] record-sets))


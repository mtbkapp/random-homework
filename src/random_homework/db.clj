(ns random-homework.db
  (:require [random-homework.process :as process]))


(defn new-db
  "Creates a new in memory database currently backed by a single Clojure atom."
  ([] (new-db #{}))
  ([data] (atom (into #{} data))))


(defn add-record
  "Adds the given record to the database."
  [db rec]
  (swap! db conj rec)
  db)


(defn sorted-records
  "Returns records from the Db db in the order specified by sort-by-option (see
  the random-homework.process namespace)"
  [db sort-by-option]
  (process/sort-records @db sort-by-option))


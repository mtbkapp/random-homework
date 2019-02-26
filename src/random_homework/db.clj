(ns random-homework.db
  (:require [com.stuartsierra.component :as c]
            [random-homework.process :as process]))


(defrecord Db
  [data]
  c/Lifecycle
  (start [this] this)
  (stop [this] this))


(defn new-db
  "Creates a new in memory database currently backed by a single Clojure atom."
  []
  (->Db (atom #{})))


(defn add-record
  "Adds the given record to the database."
  [db rec]
  (swap! (:data db) conj rec)
  db)


(defn sorted-records
  "Returns records from the Db db in the order specified by sort-by-option (see
  the random-homework.process namespace)"
  [db sort-by-option]
  (process/sort-records @(:data db) sort-by-option))


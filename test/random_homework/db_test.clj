(ns random-homework.db-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as c] 
            [random-homework.db :as db]
            [random-homework.test-recs :as tr]))


(deftest test-lifecycle
  (let [db (db/new-db)
        started (c/start db)
        stopped (c/stop db)]
    (is (= db started stopped))))


(deftest test-crud-ops
  (let [db (-> (db/new-db)
               (db/add-record tr/rec-a)
               (db/add-record tr/rec-b)
               (db/add-record tr/rec-c)
               (db/add-record tr/rec-d))]
    (is (= [tr/rec-c tr/rec-d tr/rec-a tr/rec-b]
           (db/sorted-records db :sort-by/gender)))))


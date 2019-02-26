(ns random-homework.db-test
  (:require [clojure.test :refer :all]
            [random-homework.db :as db]
            [random-homework.test-recs :as tr]))


(deftest test-crud-ops
  (let [db (-> (db/new-db)
               (db/add-record tr/rec-a)
               (db/add-record tr/rec-b)
               (db/add-record tr/rec-c)
               (db/add-record tr/rec-d))]
    (tr/compare-recs 
      [tr/rec-c tr/rec-d tr/rec-a tr/rec-b]
      (db/sorted-records db :sort-by/gender))))


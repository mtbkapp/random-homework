(ns random-homework.process-test
  (:require [clojure.test :refer :all]
            [random-homework.parse :as parse]
            [random-homework.process :as process]
            [random-homework.test-recs :as tr]))


(deftest test-sort-by-gender
  (is (= [tr/rec-c tr/rec-d tr/rec-a tr/rec-b]
         (process/sort-records tr/recs :sort-by/gender))))


(deftest test-sort-by-date-of-birth-asc
  (is (= [tr/rec-d tr/rec-c tr/rec-b tr/rec-a]
         (process/sort-records tr/recs :sort-by/date-of-birth))))


(deftest test-sort-by-last-name-desc
  (is (= [tr/rec-d tr/rec-c tr/rec-b tr/rec-a]
         (process/sort-records tr/recs :sort-by/last-name))))


(deftest test-combine-records
  (is (= [tr/rec-a tr/rec-a tr/rec-b tr/rec-b tr/rec-c tr/rec-c tr/rec-d tr/rec-d]
         (process/combine-records [tr/rec-a tr/rec-a]
                                  [tr/rec-b tr/rec-b]
                                  [tr/rec-c tr/rec-c]
                                  [tr/rec-d tr/rec-d]))))


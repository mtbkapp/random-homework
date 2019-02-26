(ns random-homework.process-test
  (:require [clojure.test :refer :all]
            [random-homework.parse :as parse]
            [random-homework.process :as process]))


(def rec-a {:gender "M" :last-name "A" :date-of-birth (parse/parse-date "09/09/1909")})
(def rec-b {:gender "M" :last-name "B" :date-of-birth (parse/parse-date "08/08/1808")})
(def rec-c {:gender "F" :last-name "C" :date-of-birth (parse/parse-date "07/07/1707")})
(def rec-d {:gender "F" :last-name "D" :date-of-birth (parse/parse-date "06/06/1606")})


(def recs [rec-a rec-c rec-b rec-d])


(deftest test-sort-by-gender
  (is (= [rec-c rec-d rec-a rec-b]
         (process/sort-records recs "gender"))))


(deftest test-sort-by-date-of-birth-asc
  (is (= [rec-d rec-c rec-b rec-a]
         (process/sort-records recs "date-of-birth"))))


(deftest test-sort-by-last-name-desc
  (is (= [rec-d rec-c rec-b rec-a]
         (process/sort-records recs "last-name"))))


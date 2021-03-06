(ns random-homework.parse-test
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string]
            [clojure.test :refer :all]
            [random-homework.parse :as parse]
            [random-homework.test-recs :as tr])
  (:import [java.io BufferedReader StringReader]))


(deftest test-date-parsing
  (let [date-str (gen/generate (spec/gen :person/date-of-birth))]
    (is (= date-str
           (parse/format-date (parse/parse-date date-str))))))


(deftest test-no-delim-str?
  (testing "no delim"
    (is (parse/no-delim-str? ""))
    (is (parse/no-delim-str? "Jason"))
    (is (parse/no-delim-str? "Kapp")))
  (doseq [[delim-name delim] parse/delims]
    (testing delim-name
      (is (not (parse/no-delim-str? (str "asdf" delim "asdf"))))
      (is (not (parse/no-delim-str? (str  delim "asdf"))))
      (is (not (parse/no-delim-str? (str "asdf" delim))))
      (is (not (parse/no-delim-str? delim)))))
  (testing "delim combo"
    (is (not (parse/no-delim-str? (apply str (vals parse/delims)))))))


(deftest test-parse-line
  (let [first-name "Jason"
        last-name "Kapp"
        gender "M"
        fav-color "green"
        date-of-birth "12/12/1912"]
    (doseq [[delim-name delim] parse/delims]
      (testing delim-name
        (is (= {:first-name first-name
                :last-name last-name
                :gender gender
                :fav-color fav-color
                :date-of-birth (parse/parse-date date-of-birth)}
               (parse/parse-line
                 delim
                 (string/join delim [last-name
                                     first-name
                                     gender
                                     fav-color
                                     date-of-birth]))))))))


(deftest test-try-parse-line
  (testing "invalid lines"
    (is (= ::spec/invalid (parse/try-parse-line "donuts donuts donuts donuts")))
    (is (= ::spec/invalid (parse/try-parse-line "donuts donuts donuts donuts donuts"))))
  (testing "comma"
    (is (= tr/rec-a (parse/try-parse-line (parse/rec->line tr/rec-a ",")))))
  (testing "pipe"
    (is (= tr/rec-a (parse/try-parse-line (parse/rec->line tr/rec-a "|")))))
  (testing "space"
    (is (= tr/rec-a (parse/try-parse-line (parse/rec->line tr/rec-a " "))))))


(deftest test-parse-file
  (let [delim "|"
        samples (gen/sample (spec/gen :person/record) 20)
        file-contents (string/join "\n" (map (partial string/join delim) samples))
        expected-records (map (partial spec/conform :person/record) samples)]
    (with-redefs [io/reader (constantly 
                              (BufferedReader. 
                                (StringReader. file-contents)))]
      (tr/compare-recs expected-records
                       (parse/parse-file "some-file" delim)))))


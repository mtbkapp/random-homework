(ns random-homework.core-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [clojure.string :as string]
            [random-homework.core :as core]))


(def invalid-path "./not-a-box-of-donuts.csv")


(deftest test-valid-file-path?
  (testing "file doesn't exist"
    (is (not (core/valid-file-path? invalid-path))))
  (testing "directory"
    (is (not (core/valid-file-path? "./src"))))
  (testing "file"
    (is (core/valid-file-path? "./test-files/comma.csv"))))


(deftest test-main
  (testing "doesn't explode"
    (let [comma-file (io/file "./test-files/comma.csv")
          pipe-file (io/file "./test-files/pipe.csv")
          space-file (io/file "./test-files/space.csv")
          output (with-out-str
                   (core/-main comma-file pipe-file space-file))]
      (is (not (string/includes? output core/usage)))))
  (testing "bad args"
    (let [output (with-out-str
                   (core/-main invalid-path invalid-path))]
      (is (string/includes? output core/usage)))))

(ns random-homework.core-test
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as spec]
            [clojure.spec.test.alpha :as stest]
            [clojure.test :refer :all]
            [random-homework.core :as core]))


(def valid-path "./test-files/pipe.csv")


(deftest test-cli-args-spec
  (testing "No args"
    (is (not (spec/valid? ::core/cli-args []))))
  (testing "File arg"
    (is (spec/valid? ::core/cli-args [valid-path "--delim" "space" "--sort-by" "last-name"]))
    (is (not (spec/valid? ::core/cli-args ["./not-a-file" "--delim" "space" "--sort-by" "last-name"]))))
  (testing "delim arg"
    (is (spec/valid? ::core/cli-args [valid-path "--delim" "space" "--sort-by" "last-name"]))
    (is (spec/valid? ::core/cli-args [valid-path "--delim" "comma" "--sort-by" "last-name"]))
    (is (spec/valid? ::core/cli-args [valid-path "--delim" "pipe" "--sort-by" "last-name"]))
    (is (not (spec/valid? ::core/cli-args [valid-path "--delim" "--sort-by" "last-name"])))
    (is (not (spec/valid? ::core/cli-args [valid-path "--delim" "fries" "--sort-by" "last-name"]))))
  (testing "sort-by arg"
    (is (spec/valid? ::core/cli-args [valid-path "--delim" "space" "--sort-by" "last-name"]))
    (is (spec/valid? ::core/cli-args [valid-path "--delim" "space" "--sort-by" "gender"]))
    (is (spec/valid? ::core/cli-args [valid-path "--delim" "space" "--sort-by" "date-of-birth"]))
    (is (not (spec/valid? ::core/cli-args [valid-path "--delim" "space" "--sort-by" "bike"])))))


(stest/instrument `core/parse-args)

(deftest test-parse-args
  (testing "both args"
    (let [args [valid-path "--delim" "pipe" "--sort-by" "last-name"]
          {:keys [op/file op/delim op/sort-by]} (core/parse-args args)]
      (is (= (io/file valid-path) file))
      (is (= "|" delim))
      (is (= "last-name" sort-by))))
  (testing "delim arg"
    (let [{comma :op/delim} (core/parse-args [valid-path "--delim" "comma" "--sort-by" "last-name"])
          {pipe :op/delim} (core/parse-args [valid-path "--delim" "pipe" "--sort-by" "last-name"])
          {space :op/delim} (core/parse-args [valid-path "--delim" "space" "--sort-by" "last-name"])]
      (is (= comma ","))
      (is (= pipe "|"))
      (is (= space " "))))
  (testing "sort-by arg"
    (let [{last-name :op/sort-by} (core/parse-args [valid-path "--delim" "comma" "--sort-by" "last-name"])
          {gender :op/sort-by} (core/parse-args [valid-path "--delim" "comma" "--sort-by" "gender"])
          {date-of-birth :op/sort-by} (core/parse-args [valid-path "--delim" "comma" "--sort-by" "date-of-birth"])]
      (is (= last-name "last-name"))
      (is (= gender "gender"))
      (is (= date-of-birth "date-of-birth")))))


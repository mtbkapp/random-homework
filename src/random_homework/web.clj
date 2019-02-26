(ns random-homework.web
  (:require [cheshire.core :as json]
            [cheshire.generate :as json-gen]
            [clojure.spec.alpha :as spec]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [random-homework.db :as db]
            [random-homework.parse :as parse])
  (:import [java.time LocalDate]))


(defn handle-get
  "Handles all the possible GET requests."
  [{:keys [db] :as request} sort-by-option]
  (db/sorted-records db sort-by-option))


(defroutes app-routes
  (GET "/records/gender" req (handle-get req :sort-by/gender))
  (GET "/records/birthdate" req (handle-get req :sort-by/date-of-birth))
  (GET "/records/name" req (handle-get req :sort-by/last-name))
  ;(POST "/" req)
  (route/not-found "¡No hay nada aquí!"))


(defn json-encode-middleware
  "Ring middleware to encode the response map's body into JSON and set the 
  Content-Type header."
  [handler]
  (fn [request]
    (-> (handler request)
        (update :body json/generate-string)
        (assoc-in [:headers "Content-Type"] "application/json"))))


(json-gen/add-encoder
  LocalDate
  (fn [date json]
    (.writeString json (parse/format-date date))))


(defn inject-db
  "Ring middleware to inject the given db on the the request map with the :db key."
  [handler db]
  (fn [request]
    (handler (assoc request :db db))))


(defn app
  "Creates the main Ring handler function for the web application."
  [db]
  (-> app-routes
      (json-encode-middleware)
      (inject-db db)))


(defn start-server
  "Starts Jetty with a fresh database listening on the given port, then blocks."
  [port]
  (jetty/run-jetty (app (db/new-db))
                   {:port port :join? true}))


(defn conform-port
  "Spec confomer. Parses a http port from a string. If the input is invalid 
  returns :clojure.spec.alph/invalid"
  [port-str]
  (try
    (let [port (Long/valueOf port-str)]
      (if (<= 1 port 65535)
        port
        ::spec/invalid)) 
    (catch NumberFormatException _
      ::spec/invalid)))


(spec/def ::cli-args
  (spec/cat :flag #{"--port"}
            :port (spec/conformer conform-port)))


(def usage "Usage: lein run -m random-homework.web --port 9090")


(defn -main
  [& args]
  (let [{:keys [port]} (spec/conform ::cli-args args)]
    (if (= ::spec/invalid port)
      (do (println usage)
          (spec/explain ::cli-args args))
      (start-server port))))


(ns riverford-statistics.server
  (:require
   [objection.core :as obj]
   [riverford-statistics.impl :as impl]
   [ring.adapter.jetty :as jetty]
   [ring.util.response :as resp]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [hiccup.core :refer [html]]))

(def index
  (html
    [:h1 "Text statistics"]
    [:form {:method "POST"
            :action "/upload"
            :enctype "multipart/form-data"}
     [:input {:type "file"
              :name "file"
              :accept "text/plain"}]
     [:input {:type "submit"
              :name "submit"
              :value "submit"}]]))

(defn upload
  [req]
  (let [{:keys [tempfile filename]} (get-in req [:params "file"])
        text (slurp tempfile)]
    (html
      [:h1 "Text statistics"]
      (for [{:keys [title value]} (impl/calculate-statistics text)]
        [:p
         [:span title]
         [:span " "]
         [:span value]]))))

(defn router
  [req]
  (case (:uri req)
    "/upload" {:status 200
               :body (upload req)}
    "/" {:status 200
         :body index}
    {:status 404
     :body "Page not found"}))

(def app
  (-> router
    wrap-params
    wrap-multipart-params))

(defn start-server
  [port]
  (-> (jetty/run-jetty #'app {:port port :join? false})
    (obj/register
      { ;; all optional
       :name (str "Jetty Server on port " port)
       :alias [:jetty-server port]
       :data {:handler #'app :port port}
       ;; optional, but wise!
       :stopfn (fn [server] (.stop server))})))

(defn -main
  [& args]
  (start-server 8080))

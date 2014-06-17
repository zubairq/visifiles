(defproject org.clojars.zubairq/coils "0.5"
  :dependencies [
                   [org.clojure/clojure "1.5.1"]
                   [org.clojure/google-closure-library-third-party "0.0-2029"]
                   [org.clojure/tools.reader "0.7.10"]
                   [org.clojure/clojurescript "0.0-2173"]
                   [om "0.6.4"]
                   [om-sync "0.1.1"]
                   [org.clojure/core.async "0.1.267.0-0d7780-alpha"]

                   [korma "0.3.0-RC5"]
                   [org.postgresql/postgresql "9.2-1002-jdbc4"]
                   [compojure "1.1.5"]
                   [shoreleave "0.3.0"]
                   [shoreleave/shoreleave-remote-ring "0.3.0"]
                   [ring "1.2.1"]
                   [ring-middleware-format "0.3.1"]
                   [ring/ring-json "0.2.0"]
                   [clojurewerkz/neocons "2.0.1"]
                   [rewrite-clj "0.2.0"]
                   [org.jasypt/jasypt "1.8"]
                   [clj-http "0.7.6"]
                   [cheshire "4.0.3"]
                   [ankha "0.1.3"]
                   [overtone/at-at "1.2.0"]
                ]
  :repositories {"sonatype-oss-public"
                 "https://oss.sonatype.org/content/groups/public/"}

  :url "http://org.clojars.zubair/coils"

  :plugins  [
               [lein-cljsbuild "1.0.2"]
               [lein-httpd "1.0.0"]
               [lein-ring "0.8.10"]
            ]

  :profiles {
                :dev
                {
                  :source-paths ["src" "../srcdev"]
                  :cljsbuild
                  {
                    :builds
                     [
                      {
                       :source-paths ["src"]
                       :compiler     {
                                       :output-to      "resources/public/main.js"
                                       :optimizations  :whitespace
                                       :output-wrapper false
                                       :externs        ["resources/public/google_maps_api_v3_11.js"]
                                       :pretty-print   false
                                     }
                      }
                     ]

                  }
                }

                :base
                {
                  :source-paths ["src"
                                 ;"srcbase"
                                 "../srcdev"
                                 ]
                  :cljsbuild
                  {
                    :builds
                     [
                      {
                       :source-paths ["src"]
                       :compiler     {
                                       :output-to      "resources/public/main.js"
                                       :optimizations  :whitespace
                                       :output-wrapper false
                                       :externs        ["resources/public/jquery-externs.js"
                                                        "resources/public/google_maps_api_v3_11.js"]
                                       :pretty-print   false
                                     }
                      }
                     ]

                  }
                }


                :test
                {
                  :source-paths ["src"
                                 "../srctest"]
                  :cljsbuild
                  {
                    :builds
                     [
                      {
                       :source-paths ["src"]
                       :compiler     {
                                       :output-to      "resources/public/main.js"
                                       :optimizations  :advanced
                                       :output-wrapper false
                                       :externs        ["resources/public/jquery-externs.js"
                                                        "resources/public/google_maps_api_v3_11.js"]
                                       :pretty-print   false
                                       :foreign-libs [{:file "https://maps.googleapis.com/maps/api/js?sensor=false"
                                                  :provides  ["google.maps" "google.maps.MapTypeId"]}]
                                     }
                      }
                     ]

                  }
                }

                :prod
                {
                  :source-paths ["src" "../srcprod"]
                  :cljsbuild
                  {
                    :builds
                     [
                      {
                       :source-paths ["src"]
                       :compiler     {
                                       :output-to      "resources/public/main.js"
                                       :optimizations  :advanced
                                       :output-wrapper false
                                       :externs        ["resources/public/jquery-externs.js"
                                                        "resources/public/google_maps_api_v3_11.js"
                                                        "resources/public/reactextern.js"]
                                       :pretty-print   false
                                       :foreign-libs [{:file "https://maps.googleapis.com/maps/api/js?sensor=false"
                                                  :provides  ["google.maps" "google.maps.MapTypeId"]}]
                                     }
                      }
                     ]

                  }
                }
            }


  :ring {:init       webapp.framework.server.core/init
         :handler    webapp.framework.server.core/app}


)

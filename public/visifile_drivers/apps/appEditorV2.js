{
    doc_type: 'appshare',
    name: 'appEditorV2',version: 1,



    events: {
        "This will return the editor app": {
            on: "app",
            do:    function(args, returnfn) {
                is_app()

                var argAppName = args.appName

                Vue.component("app_editor",
                {
                  template: `<div>
                    Okhay this AppShare app editor
                        Code ID: {{code_id}}
                        <br>
                        <br>

                        <component  is="editor_component" v-if="editor_loaded">
                                        <button slot-scope="editor_component"
                                                v-on:click='save(code_id, editor_component.text)'
                                                type="button" class="btn btn-primary">

                                                    Save
                                       </button>
                        </component>

                        <component  v-bind:is="app_name" v-if="app_loaded">
                          APP HERE
                        </component>

                        <div  v-if="!app_loaded">
                            No app selected. Select one:
                            <br>
                            <select @change="chooseApp">
                                <option v-for="item in apps" v-bind:value="item">{{item}}</option>
                            </select>

                        </div>
                  </div>
                   `
                   ,
                   data: function() {
                       return {
                           editor_loaded: false,
                           app_loaded: false,
                           apps: [],
                           app_name: null,
                           code_id: "..."
                       }
                   }
                   ,
                   methods: {
                       chooseApp: function(event) {
                            //alert(event.target.value)
                            this.load_app(event.target.value)
                       },




                       save: function(code_id, text) {
                           var mm = this
                           //alert("Saving " + code_id)
                           //alert("Saving " + text)
                           callDriverMethod(
                               "appEditorV2",  "saveCode",
                               {
                                   code: text,
                                   code_id: code_id
                               }
                               ,
                               function(results) {
                                //zzz
                                   alert("Reloading: " + mm.app_name)
                                   mm.load_app(mm.app_name)
                               })
                       },





                       load_app: function (appName) {
                           //alert("trying to load app: " + appName)
                           var mm = this
                           mm.code_id = args.code_id
                           callDriverMethod(
                               appName,  "app",
                               {
                               }
                               ,
                               function(results) {
                                   mm.app_loaded = true
                                   mm.app_name = results.name
                                   //alert(results.name + " loaded")
                               })



                           var sql =    "select  id, code  from  system_code  where " +
                                        "     on_condition like '%app%' and driver = '" + appName + "'"

                           //alert( sql )

                           callDriverMethod(
                               "systemFunctions",  "sql",
                               {
                                   sql: sql
                               }
                               ,
                               function(results) {
                                   if (results) {
                                       //alert(results.value.length)
                                       if (results.value.length > 0) {
                                           var code = results.value[0].code
                                           var codeId = results.value[0].id
                                           //alert(code)
                                           mm.code = code
                                           mm.code_id = codeId
                                           //alert(code)
                                           callDriverMethod(
                                               "editorComponent",  "component",  {text: code}
                                               ,
                                               function(result) {
                                                    mm.editor_loaded = true })
                                       }
                                   }
                               })





                           }

                   },

                   mounted: function () {
                       var mm = this
                       if (argAppName) {
                            this.load_app(argAppName)
                        } else {
                            callDriverMethod( "systemFunctions",
                                              "get_apps_list"
                                              ,{}
                                        ,
                                        function(result) {
                                          //  console.log("3) returned result: " + JSON.stringify(result,null,2))
                                            mm.apps = result.value
                                        })
                        }
                   }
                   })
                   returnfn({name: "app_editor"})







            }, end: null
        }

        ,


        "Save the editor code": {
            on: "saveCode"
            ,
            do: function(args,returnfn) {
                console.log("Saving in Sqlite: " + args.code_id)
                console.log("Saving in Sqlite: " + args.code)
                var stmtUpdateCode = dbsearch.prepare(" update   system_code   set code = ? where id = ?");
                    dbsearch.serialize(function() {
                    dbsearch.run("begin exclusive transaction");
                    stmtUpdateCode.run(
                          args.code,
                          args.code_id)

                    dbsearch.run("commit");
                    stmtUpdateCode.finalize();

                })
                returnfn({})
            }
            ,
            end: null

        }

    }

}


void call(def message) {
    echo 'Maven process ' + groovy.json.JsonOutput.toJson(message)
}

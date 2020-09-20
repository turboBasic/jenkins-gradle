
Map parseJsonString(String json) {
    new groovy.json.JsonSlurperClassic()
        .parseText(json)
}

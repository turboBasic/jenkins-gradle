
void sendEmail(def message) {
    echo 'Send email to ' + groovy.json.JsonOutput.toJson(message)
}

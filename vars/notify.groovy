def call(String credentialsId = 'discord-webhook') {
  withCredentials([string(credentialsId: credentialsId, variable: 'DISCORD_URL')]) {
    def status = currentBuild.currentResult
    def icon = status == 'SUCCESS' ? '✅' : (status == 'UNSTABLE' ? '⚠️' : '❌')
    def msg = "${icon} ${env.JOB_NAME} #${env.BUILD_NUMBER} : ${status} ${env.BUILD_URL}"
    writeFile file: 'discord.json', text: groovy.json.JsonOutput.toJson([content: msg])
    sh '[ -n "$DISCORD_URL" ] && curl -sS -H "Content-Type: application/json" -d @discord.json "$DISCORD_URL" || true'
  }
}
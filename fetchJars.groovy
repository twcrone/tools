class FetchJarsTests extends GroovyTestCase {

    def nexusServerUri = 'http://'
    def resolvePath = '/service/local/artifact/maven/resolve'
    def contentPath = '/content/groups'
    def repo = 'sprn-maven2'
    def groupId = 'com.bravo12.project'
    def version = '0.1-SNAPSHOT'
    def ext = 'jar'
    def dir = 'lib'
    def artifactIds = ['blah-blah', 'blah-blah']

    void test_downloadArtifacts() {
        artifactIds.each { artifactId ->
            println "Downloading ${artifactId}..."
            downloadArtifactAndAssertLastModified(artifactId)
        }
        println "Done."
    }

    def downloadArtifactAndAssertLastModified(artifactId) {
        def originalModificationTime = getArtifactFile(artifactId).lastModified()
        downloadArtifact(artifactId)
        def currentModificationTime = getArtifactFile(artifactId).lastModified()
        assert currentModificationTime > originalModificationTime
    }

    def downloadArtifact(artifactId) {
        def repoPath = getRepoPath(artifactId)
        def address = "${nexusServerUri}${contentPath}/${repo}${repoPath}"
        def file = getArtifactFile(artifactId)
        write(address, file)
    }

    def getArtifactFile(artifactId) {
        def filename = "${artifactId}-$version.${ext}"
        new File(dir, filename)
    }

    def getRepoPath(artifactId) {
        def artifactResUrl = "${nexusServerUri}${resolvePath}?r=$repo&g=$groupId&a=$artifactId&v=$version&e=$ext"
        def artifactRes = new XmlSlurper().parse(artifactResUrl)
        artifactRes.data.repositoryPath
    }

    static write(String address, File file) {
        def fos = new FileOutputStream(file)
        def out = new BufferedOutputStream(fos)
        out << new URL(address).openStream()
        out.close()
    }
}

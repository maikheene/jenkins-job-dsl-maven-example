package utilities.git

class GitBranchReader {
    
    def getGitRepositoryBranches(String sshKey, String gitURL) {
        add(sshKey)

        def command = "git ls-remote -h $gitURL"

        def proc = command.execute()
        proc.waitFor()

        removeAll()
        def branches = proc.in.text.readLines().collect { 
            it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '') 
        }

        return branches 
    }
    
    void add(String keyPath) {
        def output = ['ssh-add', keyPath].execute().waitForProcessOutput(System.out, System.err)
        println(output)
    }

    void removeAll() {
        ['ssh-add', '-D'].execute().waitForProcessOutput(System.out, System.err)
    }
}


# Scripts

Convenience scripts.

## Overview

Convenience scripts for development tasks.

## `alter-path.sh`

The `alter-paths.sh` script will spin through a directory tree.
It will rename any folders identified by the `--orig-tl` argument to the name specified by `--tl`.
The same is true for `--orig-org` and `--org` respectively.

```bash
scripts/alter-path.sh [--path <path>] [--tl <toplevel>] [--org <organization>] [--orig-tl <original toplevel>] [--orig-org <original org>]
  --path        path to process (.)
  --tl          top level package name (com)
  --org         org level package name (example)
  --orig-tl     top level package name (io)
  --orig-org    org level package name (twdps)
  --help        display this help
```

### Example Usage

Rename the current `io.twdps` package name to `com.example`

```bash
% ./scripts/alter-path.sh --orig-tl io --tl com --orig-org twdps --org example
```

## `apply-sed.sh`

The `apply-sed.sh` script will apply the given sed-script (defined in the named file) to a set of files.

```bash
scripts/apply-sed.sh [--sed <script>] [--preserve <ext>] [--file <file>] [--path <path>] [--tree <tree>]
  --sed         sed script to execute ()
  --preserve    preserve translated files with this extension for debugging ()
  --file        file to process ()
  --path        process all files in path non-recursively ()
  --tree        process all files in tree recursively ()
  --help        display this help
  NOTE: files are processed in the order specified on the command line
        The --sed script and --preserve flag must be specified before any files/paths
```

### Example Usage

Apply the `foo.sed` script to the directory tree, and save the original file with the `.orig` extension.

```bash
% ./scripts/apply-sed.sh --sed foo.sed --preserve orig --tree .
```

## `create-lib.sh`

The `create-lib.sh` script create a new empty sub-module package directory with an empty skeleton.
The skeleton will contain `src/main/*` and `src/test/*` paths for the specified package.
The script will also create an empty `build.gradle` file as a starting point.

```bash
scripts/create-lib.sh [--path <path>] [--tl <toplevel>] [--org <organization>] [--pkg <package name>]
  --path        path to create new package (.)
  --tl          top level package name (io)
  --org         org level package name (twdps)
  --pkg         package name ()}
  --help        display this help
```

### Example

Use the `create-lib.sh` script to create a new sub-module named `foobar`:

```bash
% scripts/create-lib.sh --tl io --org twdps --pkg foobar
```

## `rebrand.sh`

The `rebrand.sh` script will copy the project to a new project.
It will also rename the package paths (directory paths and code).

Given the option, it will also delete the local `.git` repository folder, effectively disconnecting its history from the parent.

```bash
scripts/rebrand.sh [--path <path>] [--dst <dest path> ] \
    [--repo <reponame>] [--gh-org <github Org name>] \
    [--tl <toplevel>] [--org <organization>] \
    [--orig-repo <reponame>] [--orig-gh-org <github Org name>] \
    [--orig-tl <original toplevel>] [--orig-org <original org>] \
    [--nuke-git] [--clear] [--test]
  --path        path to process (.)
  --dst         copy to destination (no in-place mods) ()
  --repo        repository name (starter-boot)
  --gh-org      github organization name [or username] (example)
  --tl          top level package name (com)
  --org         org level package name (example)
  --group       group level package name (starter)
  --prefix      prefix for repositories (dx)
  --orig-repo   original repository name (starter-boot)
  --orig-gh-org original github organization name [or username] (thoughtworks-dps)
  --orig-tl     original top level package name (io)
  --orig-org    original org level package name (twdps)
  --orig-group  original group level package name (starter)
  --orig-prefix original prefix for repositories (dps)
  --nuke-git    remove .git repository folder (n)
  --clear       clear destination directory of current contents
  --test        copy to temporary directory to test
  --help        display this help
```

### Example Usage

Rebrand this starter-boot to a generic project:

```bash
% scripts/rebrand.sh \
  --dst ../dx-starter-boot \
  --repo example-starter-boot \
  --gh-org example \
  --tl com \
  --org example \
  --orig-repo dps-starter-boot \
  --orig-gh-org thoughtworks-dps \
  --orig-tl io \
  --orig-org twdps \
  --nuke-git 
```

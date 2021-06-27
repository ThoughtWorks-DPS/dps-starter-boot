# Documentation

Documentation is created in Markdown (Github flavor) and rendered by MkDocs.

## Getting started

Start to write your documentation by adding more markdown (`*.md`) files to the documentation folder (`./docs`) or replace the content in existing files.

## Site navigation

For new pages to appear in the left-hand navigation you need edit the `mkdocs.yml` file in root of your repo.
The navigation can also link out to other sites.

Alternatively, if there is no `nav` section in `mkdocs.yml`, a navigation section will be created for you.
However, you will not be able to use alternate titles for pages, or include links to other sites.

Note that MkDocs uses `mkdocs.yml`, not `mkdocs.yaml`, although both appear to work.
See also <https://www.mkdocs.org/user-guide/configuration/>.

## Table of Contents

The Table of Contents is generated automatically based on the hierarchy of headings within each document.
The TOC will appear depending on the deployment target.  

### Backstage

When rendering to Backstage, one must uncomment the following lines in the mkdocs.yml file:

```yaml
#plugins:
#  - techdocs-core
```

The repository must also be registered as a component within Backstage.
That will allow the Backstage services to generate documentation using the ./mkdocs.yml file in the project root folder.

The Table of Contents will appear in the right sidebar.

### Github Pages

When documentation is generated and deployed to Github Pages, the Table of Contents will appear on the left sidebar.
Sub-headings will stay hidden unless the parent heading is specifically opened (via the '+' hover icon on the left of each heading)

## Support

If you need support, the DPS channel is helpful: [#docs-like-code](https://slack.com/channels/) on Slack.

# Reproducing an issue with GitHub's IP allowlist feature

GitHub provides a feature for Enterprise orgs to define an IP allowlist. Requests to the GitHub org's API will be blocked if the client IP address is not in that list.

Working on the GitHub for Jira app, we have identified some issues with this feature. In this repo, I want to document these issues and how to reproduce them.

## Steps to reproduce

### Create a tunnel
For the OAuth dance between GitHub and the test app to work, you need to expose port 8080 to the internet:

You can use ngrok for this ([setup instructions](https://dashboard.ngrok.com/get-started/setup)).

Once ngrok is setup, run `ngrok http 8080`

### Create a GitHub app

Go to https://github.com/settings/apps and create a new GitHub app:

* as callback URL, enter the URL that ngrok gives you, plus the suffix `/callback`.
* disable "expire user authorization tokens"
* disable the "Active" checkbox under "webhooks"
* give "read-only" access for the "Metadata" permission
* after hitting "Create", on the following page click the button "Generate a private key" and save the .pem file for later.
* from the "General" tab of your app, copy the "App ID" and "Client ID" for later use

### Install the GitHub app into an org

* in the settings of your GitHub app, click "Install App"
* choose one of your orgs and click on "Install"
* follow the prompts


### Run the test app

* clone this repo
* copy the private key you downloaded earlier into the file `private-key.pem` in the main folder of the repo
* copy the "App ID" and "Client ID" into the respective fields in the file `src/main/resources/application.yml`

TODO: describe how to run the test app

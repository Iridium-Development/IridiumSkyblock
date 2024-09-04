# Contribution Guidelines

This document aims to outline our expectations of potential contributors, as well as to explain the project structure and answer some common questions.



Table of Contents:

> - Code of Conduct
> 
> - Project Structure
>   
>   - Selecting a Repo
> 
> - Feature Requests
> 
> - Opening an Issue
> 
> - Submitting Pull Requests (PRs)
> 
> - Translation
> 
> - Style Guidelines
> 
> - Code Reviews



## Code of Conduct

We expect ALL contributors to act in a respectful and inclusive manner, regardless of background or ideology. As such, we have put together conduct guidelines that potential contributors should adhere to.



> - Be respecful and inclusive - discrimination, including but not limited to racism, sexism (transphobia), homophobia, hate speech (slurs), or NSFW content of any kind are not wished to be seen, and will most likely result in restrictions or a ban from the repo.
> 
> - Stay on topic - the GitHub is specifically for contributing to the project. If you'd like to join a community space, we have a Discord server, we'd love to have you!
> 
> - Do not bump or spam issues, especially unrelated conversations.
> 
> - Advertising on the repo, or using private messages to do so is prohibited. Advertising includes but is not limited to Discord servers, Minecraft servers, service threads, and companies.
> 
> - Services and recruitment are not to be requested in this repo.
> 
> - Light profane language, such as light swearing can be used moderately. Excessive swearing is not allowed.
> 
> - You are prohibited to share links from unknown sources which may contain any banned content. Automatic downloads or malicious files are not excluded from this rule. This also includes files that are uploaded to the repo.
> 
> - Organization members have the final say, if you think their decision is unfair, contact PeachesMLG (@peaches_mlg on Discord).



## Project Structure

IridiumSkyblock is divided into a couple of different repos, with a project hierarchy that must be adhered to.

You may notice when compiling and developing against 
IridiumSkyblock that there is a significant portion of code that isn't 
located in this repo. That's because IridiumSkyblock is an extension of 
IridiumTeams, and also uses functions from IridiumCore.

- [IridiumCore](https://github.com/Iridium-Development/IridiumCore)
  - A sort of library for all of Iridium Development's plugins
- [IridiumTeams](https://github.com/Iridium-Development/IridiumTeams)
  - The generic plugin, which extends IridiumCore, and involves all of 
    the code for team management, including leveling, missions, team 
    members, the bank, etc.
- [IridiumSkyblock](https://github.com/Iridium-Development/IridiumSkyblock)
  - This plugin, which extends IridiumTeams, and houses its own code specific to Skyblock, such as the world generation.

### Selecting A Repo

The code or feature that you are planning to implement should have its use case / features considered before you select a repo to contribute to.

> - If the contribution is a "backend", meta, or version specific change, it will likely go into IridiumCore. Examples include player skull handling, NMS, GUI changes, and item creation.
> 
> - If the contribution is a general "frontend" or feature request that works with several of our plugins or is NOT specific to the skyblock gamemode, it will likely go into IridiumTeams.
> 
> - If the contribution is a specific "frontend" or feature request that works only with IridiumSkyblock or is specific to the skyblock gamemode, it will likely go into IridiumSkyblock.

If you're ever unsure, you can always reach out on Discord, we're happy to give you a helping hand.



## Feature Requests

- For requests, fill out an issue with the feature request tag selected and explain in detail the use case for this feature.

- For PRs, submit a PR using our Pull Requests Guidelines and explain in detail the use case and the expected behavior for the feature. You are also expected to highlight any known issues or otherwise specific details about your PR, and if you need help, to explain what has you stumped.

Here is a Feature Request template that you can use for your requests, if you so choose.

> **What kind of feature would you like to see in the plugin?**
> 
> Explain the feature in detail, and how you think it should work.
> 
> **Why do you want this feature?**
> 
> Explain why this feature would benefit you.
> 
> **How would you use this feature?**
> 
> Detail how this feature would work on your server.
> 
> **How do you think other server owners would use this feature?**
> 
> Detail how this feature would work on other servers or in general, and how that would benefit them.



## Opening an Issue

If you've discovered a bug in IridiumSkyblock, you should open an issue on our repo. If this issue is with IridiumTeams or IridiumCore, organization members will re-create the issue on the correct repo and link to your issue.

Here is an Issue template that you can use for your issues, if you so choose.

> **Describe the bug**
> A clear and concise description of what the bug is.
> 
> **To Reproduce**
> Steps to reproduce the behavior:
> 
> 1. Go to '...'
> 2. Click on '....'
> 3. Scroll down to '....'
> 4. See error
> 
> **Expected behavior**
> A clear and concise description of what you expected to happen.
> 
> **Server and Plugin Version**
> Copy and paste the results of `/version` and `/is about`
> 
> **Screenshots**
> If applicable, add screenshots to help explain your problem.
> 
> **Logs**
> If there are error messages in your console, please send them via [this](https://mclo.gs)
> 
> **Additional context**
> Add any other context about the problem here.



## Submitting Pull Requests (PRs)

We ask that all code contributions are submitted via PRs. They should be explicit, detailed, and their scope narrowed to resolving a specific problem or adding a specific feature. In the case of overhauls, everything related to the overhaul should be contained within a single PR. Try to keep PRs pure by separating quick fixes and extras into their own PRs.

Here is a Pull Request template that you can use for your PRs, if you so choose.

> **Describe the contribution**
> 
> A clear and concise description of the changes made by the PR.
> 
> **Explain the Issue**
> 
> If applicable, link the issue that you're addressing and explain what the problem is, as well as how your contribution fixes it.
> 
> **Expected Behavior**
> 
> Outline the contribution's expected beahvior and affects.
> 
> **Tested Versions**
> 
> The server versions you've tested this fix with. Please specify whether you're on Spigot or Paper, and their associated build versions.
> 
> **Known Issues**
> 
> If applicable, describe any known issues with your contribution or issues you came across while working on it.
> 
> **Dependencies**
> 
> Any specific dependencies of your PR, including any PRs submitted to IridiumTeams or IridiumSkyblock.



## Translation

IridiumSkyblock accepts translations in the form of translated default configuration files (.yml). If you contribute a translation, please create a PR with changes only in the configuration files and the translation tag applied. If accepted, we will close the PR and your translation will show up on our wiki.



## Style Guidelines

peaches you can write this one



## Code Reviews

All code contributions must go through a mandatory review via one of the organization members. If your code does not build, it will not be accepted until changes are made to allow it to do so, with the exception of PRs that rely on changes made to IridiumTeams or IridiumCore (these PRs MUST be linked in your PR).

Code reviews will typically be done by PeachesMLG, but may also be conducted by other organization members. These reviews will consider the style guidelines, the efficiency of your code, API usage, and scrutinize decisions made. If approved, your PR will then be merged; otherwise, someone will leave a comment with feedback or a suggestion on your PR.

If you decide that you want to make multiple contributions, and you are a part of our Discord server, please reach out to a staff member regarding our contributor role.



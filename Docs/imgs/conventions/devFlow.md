# Workflow

## Commit msgs:

commit msgs should take the following format

```
<type> (<scope>): <title> <(optional) description> 

```

### available types:
 
- :bug: -> fixing bugs
- :sparkles: -> new features
- :tada: -> start a project
- :memo: -> update or add documentation
- :lipstick: -> update styling
- :construction: -> work in progress (unfinished features)
- :recycle: -> refactor code
- :thread: -> multihreading
- :boom: -> introduce breaking changes

for more:
[refrence](https://gitmoji.dev/)


### Gitmoji Examples with Descriptions


**1.  :bug:** Fixing Bugs

* **Example:** `:bug: Fixed memory leak in login function`


**2. ✨ :sparkles:** New Features

* **Example:** `:sparkles: Implemented user profile customization options`

**3.  :tada:** Starting a Project

* **Example:** `:tada: Initial commit for new shopping cart system`

**4.  :memo:** Updating or Adding Documentation

* **Example:** `:memo: Updated API reference documentation for v2.0 release`

**5.  :lipstick:** Updating Styling

* **Example:** `:lipstick: Adjusted button color scheme for better accessibility`

**6. ️ :construction:** Work in Progress (Unfinished Features)

* **Example:** `:construction: WIP - Implementing user authentication flow`

## TO SETUP GITMOJI

```npm install -g gitmoji-cli```

and instead of your usual ```git commit```

write ```gitmoji -c```




## Considering branching: 
Thanks to [amir-kedis](https://github.com/amir-kedis) for this way!


1. You will make a new branch based on the latest main branch with the feature name (or fix)
2. You will work until you finish with the flow you want but...
3. You will first rebase main -> your-branch (if you wish here you could make to interactive rebase to make sure the commit msgs and number of commits are reasonable)
4. after you solve all the conflicts and make sure everything is OK,
5. You will fast-forward merge your branch into main.


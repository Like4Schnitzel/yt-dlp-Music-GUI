## For this to run you need to have the following installed:
+ [<u>**yt-dlp**</u>](https://github.com/yt-dlp/yt-dlp#installation)
+ [<u>**ffmpeg**</u>](https://ffmpeg.org/download.html)

You can download the Windows release which comes with executables for both of them and pre-configured path variables.

## yt-dlp-Music-GUI
A simple cross platform GUI for downloading songs from YouTube with [<u>yt-dlp</u>](https://github.com/yt-dlp/yt-dlp).
The goal of this project was to stop having to think about which arguments to pass every time I want to download a song.

Current features are:
+ Automatic detection for artist name, album name and title/track name
+ Manually setting artist name, album name and title/track name
+ Automatic metadata embedding
+ Downloading playlists
+ Partially downloading playlists from one index to the other
+ Only downloading the given song from a playlist
+ Setting the album name to single, making it the same as the title name (works for playlists)
+ Automatically switching to video title if there is no track name available
+ Automatically switching to playlist name if there is no album available
+ Automatically switching to channel name if there is no artist available
+ A progress bar for the download of both videos and playlists
+ Setting the main download directory (defaults to the user's Music directory)
+ Making the filenames fit with naming conventions while keeping the metadata unchanged (you can adjust which characters to replace in the config)
+ An easy way to configure things
+ Downloading chapters
+ Downloading from a specified timestamp to another
+ Keeping playlist indices as track numbers
+ Regex for titles in playlists

Planned features:
+ Adding more config variables upon demand
+ Improving flexibility of the UI to allow for better scaling

## How to use regex for playlists
When the option to download as a playlist is selected you will be able to enter regex into the title input field.\
Regexes are marked with a forward slash '/' at the start and end.
Any number of regexes can be used.
All used regexes must contain exactly one non-empty capture group, the content of which will then be added to the title.
Within a regex you must escape forwards slashes with a backwards slash, like so: "\\/".\
Anything outside the marked regexes will be added to the captured strings as constants.\
**Example**: The input `/(.*) \[/ - Tyson Yen Cover` when used on
[<u>this video</u>](https://www.youtube.com/watch?v=kzVAtMYwCCE&list=PL6NhlkyT-fwyW-hwgLTvcYN1Bx37RxBIF&index=30 "The Time Has Come [Devil May Cry 4] Cover")
will result in the title "The Time Has Come - Tyson Yen Cover".


## Config file documentation
You can create a file named `config.txt` in the same directory as the .jar file to specify a few variables. The syntax is as follows:
```
variable-name=value
```
Empty lines get ignored, lack of an equals sign will result in an error, causing the line to get ignored. Spaces before and after the sign will not be ignored.
If the file could not be found or a variable that is used in the program hasn't been assigned in the file, it instead assumes its default value. Below is a template `config.txt` file with all currently available variables and their default values.
```
ffmpeg-path=ffmpeg
yt-dlp-path=yt-dlp

delay-seconds=1
replace-chars=<>:"/\|?*
file-format-options=default (best audio);.opus;.mp3;.ogg;.m4a

frame-width=620
frame-height=350
left-bound=10
upper-bound=20
line-distance=25
```
Explanation as to what these variables do:
## Path Options:
    ffmpeg-path       The path to your ffmpeg executable. If you wish to use the version that is already installed 
                      on your system, leave it as default. If you want to specify a path relative to the directory
                      the .jar file is located in, put "./further/file/path/ffmpeg.exe".
    yt-dlp-path       Same as `ffmpeg-path`, but replace "ffmpeg" with "yt-dlp".
## Utility Options:
    delay-seconds     The amount of delay before the validity of the URL that is entered is checked. This timer
                      gets reset whenever the text field receives a change. This is in place to make editing said
                      field possible.
    replace-chars     Specify characters in the file name to replace with underscores to deal with naming conventions.
                      If you put spaces inbetween the characters it will replace all spaces with underscores.
    file-format-options A list of possible formats to convert to after downloading, separated by semicolons.
                      If "default (best audio)" is selected, it lets yt-dlp automatically select the bestaudio format.
                      Essentially no input validation is done here, it's your responsibility to check that all custom
                      file formats are valid. If you want the crashes to be handled, submit a PR.
## GUI Options:
    frame-width       Specifies the width of the main window in pixels.
    frame-height      Specifies the height of the main window in pixels.
    left-bound        Specifies the space between elements on the main window and the left border in pixels.
    upper-bound       Specifies the space between elements on the main window and the upper border in pixels.
    line-distance     Specifies how many pixels a line should take up vertically.

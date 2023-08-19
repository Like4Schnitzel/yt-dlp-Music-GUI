## For this to run you need to have the following installed:
+ [**yt-dlp**](https://github.com/yt-dlp/yt-dlp#installation)
+ [**ffmpeg**](https://ffmpeg.org/download.html)

You can download the Windows release which comes with executables for both of them and pre-configured path variables.

## yt-dlp-Music-GUI
A simple cross platform GUI for downloading songs from YouTube with [yt-dlp](https://github.com/yt-dlp/yt-dlp).
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
+ Making the filenames fit with naming conventions while keeping the metadata unchanged (you can adjust which characters to replace in the function `formatForFileName(String str)` in `Loader.java`)
+ An easy way to configure things
+ Packaging yt-dlp and ffmpeg with the .jar file
+ Downloading chapters
+ Downloading from a specified timestamp to another
+ Keeping playlist indices as track numbers

Planned features:
+ Adding more config variables upon demand
+ Auto downloading dependencies
+ Improving flexibility of the UI to allow for better scaling
+ Regex for titles in playlists

## Config file documentation
You can create a file named `config.txt` in the same directory as the .jar file to specify a few variables. The syntax is as follows:
```
variable-name=value
```
Empty lines get ignored, lack of an equals sign will result in an error, causing the line to get ignored. Spaces before and after the sign will not be ignored.
If the file could not be found or a variable that is used in the program hasn't been assigned in the file, it instead assumes it's default value. Below is a template of a `config.txt` file with all currently available variables and their default values.
```
ffmpeg-path=ffmpeg
yt-dlp-path=yt-dlp

delay-seconds=1
replace-chars=<>:"/\|?*

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
## GUI Options:
    frame-width       Specifies the width of the main window in pixels.
    frame-height      Specifies the height of the main window in pixels.
    left-bound        Specifies the space between elements on the main window and the left border in pixels.
    upper-bound       Specifies the space between elements on the main window and the upper border in pixels.
    line-distance     Specifies how many pixels a line should take up vertically.

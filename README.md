# For this to run you need to have Java 11 or higher and yt-dlp installed. You can check how to do so [**here**](https://github.com/yt-dlp/yt-dlp#installation).

### simple-GUI-for-yt-dlp
A simple cross platform GUI for downloading songs from YouTube with [yt-dlp](https://github.com/yt-dlp/yt-dlp).
The goal of this project was to stop having to think about which arguments to pass every time you want to download a song.

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

Planned features:
+ Setting the main output directory (currently downloads to the user's music directory)

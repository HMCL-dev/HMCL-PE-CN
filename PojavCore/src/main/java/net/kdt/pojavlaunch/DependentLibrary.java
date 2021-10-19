package net.kdt.pojavlaunch;

public class DependentLibrary {
    public String name;
	public LibraryDownloads downloads;
    public String url;
    
	public static class LibraryDownloads
	{
		public JMinecraftVersionList.MinecraftLibraryArtifact artifact;
		public LibraryDownloads(JMinecraftVersionList.MinecraftLibraryArtifact artifact) {
			this.artifact = artifact;
		}
	}
}


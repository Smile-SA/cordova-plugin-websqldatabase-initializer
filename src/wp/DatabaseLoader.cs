using System;
using System.Diagnostics;
using System.IO;
using System.Windows;

using System.IO.IsolatedStorage;
using SharpGIS;

namespace org.smile.websqldatabase
{
    public class DatabaseConfig
    {
        private String databaseZippedName;
        private String databaseName;
        private String databaseJournal;
        private bool forceReload = false;

        /// <param name="databaseZippedName">The name of the zip file containing the database</param>
        /// <param name="databaseName">The name of the ".db" file contained in the zip file</param>
        public DatabaseConfig(String databaseZippedName, String databaseName)
        {
            this.databaseZippedName = databaseZippedName;
            this.databaseName = databaseName;
            this.databaseJournal = databaseName + "-journal";
        }

        public String DatabaseZippedName
        {
            get
            {
                return databaseZippedName;
            }
        }

        public String DatabaseName
        {
            get
            {
                return databaseName;
            }
        }

        public String DatabaseJournal
        {
            get
            {
                return databaseJournal;
            }
            set
            {
                databaseJournal = value;
            }
        }

        public bool ForceReload
        {
            get
            {
                return forceReload;
            }
            set
            {
                forceReload = value;
            }
        }
    }

	public class DatabaseLoader
	{
        private DatabaseConfig databaseConfig;

        public void LoadDatabase(DatabaseConfig databaseConfig)
        {
            this.databaseConfig = databaseConfig;
            try
            {
                CreateDbJournalFileInIsolatedStorage();
                CopyDbFileFromAppToIsolatedStorage();
            }
            catch (IOException e)
            {
                Debug.WriteLine(e.StackTrace);
            }
        }

        private void CreateDbJournalFileInIsolatedStorage()
        {
            String databaseJournal = databaseConfig.DatabaseJournal;

            using (IsolatedStorageFile isoStore = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (isoStore.FileExists(databaseJournal) && !databaseConfig.ForceReload)
                {
                    Debug.WriteLine("File " + databaseJournal + " already exists. Creation aborted.");
                }
                else
                {
                    if (databaseConfig.ForceReload)
                    {
                        Debug.WriteLine("ForceReload is true: the database journal will be reinitialized.");
                        isoStore.DeleteFile(databaseJournal);
                    }

                    Debug.WriteLine("Creating file " + databaseJournal);
                    isoStore.CreateFile(databaseJournal).Close();
                }
            }
        }

        private void CopyDbFileFromAppToIsolatedStorage()
        {
            using (IsolatedStorageFile isoStore = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (isoStore.FileExists(databaseConfig.DatabaseName) && !databaseConfig.ForceReload)
                {
                    Debug.WriteLine("File " + databaseConfig.DatabaseName + " already exists. Copy aborted.");
                }
                else 
                {
                    if (databaseConfig.ForceReload)
                    {
                        Debug.WriteLine("ForceReload is true: the database will be overriden.");
                    }

                    using (Stream input = Application.GetResourceStream(new Uri(databaseConfig.DatabaseZippedName, UriKind.Relative)).Stream)
                    {
                        Unzip(input);
                    }
                }
            }
        }

        private void Unzip(Stream stream)
        {
            using (IsolatedStorageFile isoStore = IsolatedStorageFile.GetUserStoreForApplication())
            {
                using (var zipStream = new UnZipper(stream))
                {
                    foreach (string file in zipStream.FileNamesInZip)
                    {
                        string fileName = System.IO.Path.GetFileName(file);

                        if (!string.IsNullOrEmpty(fileName))
                        {
                            Debug.WriteLine("Extracting " + fileName + " ...");

                            //save file entry to storage
                            using (var streamWriter =
                                new BinaryWriter(new IsolatedStorageFileStream(fileName,
                                                                               FileMode.Create,
                                                                               FileAccess.Write, FileShare.Write,
                                                                               isoStore)))
                            {
                                Stream fileStream = zipStream.GetFileStream(file);

                                var buffer = new byte[2048];
                                int size;
                                while ((size = fileStream.Read(buffer, 0, buffer.Length)) > 0)
                                {
                                    streamWriter.Write(buffer, 0, size);
                                }
                            }
                        }
                    }
                }
            }
        }
	}
}
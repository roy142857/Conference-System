package gateway;

import controller.session.Session;

import java.io.*;

/**
 * Session IO serialize/deSerialize a Session object.
 */
public class SessionIO{
	
	/**
	 * Restore the Session, return null if failed.
	 */
	public Session restoreSession(String path) throws ClassNotFoundException, IOException{
		InputStream file = new FileInputStream(path);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream(buffer);
		
		// deserialize the session
		Session session = (Session) input.readObject();
		input.close();
		return session;
	}
	
	/**
	 * Save the Session, return false if failed.
	 */
	public void saveSession(String path, Session session) throws IOException{
		OutputStream file = new FileOutputStream(path);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		
		// serialize the session
		output.writeObject(session);
		output.close();
	}
}

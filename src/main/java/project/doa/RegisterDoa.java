package project.doa;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import project.model.PasswordEncryption;
import project.model.RegisterDetail;
import com.EmployeeManagementSystem.model.PasswordEncryptionWithAes;

public class RegisterDoa {
	/*
	 * private static final String
	 * dbUrl="jdbc:mysql://localhost:3306/ad_programming_cw"; private static
	 * final String dbUsername="root"; private static final String
	 * dbPassword="root";
	 */
	private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

	private static final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
	private static final int IV_LENGTH_BYTE = 12;
	private static final int SALT_LENGTH_BYTE = 16;
	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	private static final String register_customer = "insert into customer(firstname,lastname,address,phone,email,image) values (?,?,?,?,?,?)";
	private static final String view_all_customer = "select * from customer";
	private static final String update_customer = "update customer set firstname=?,lastname=?,"
			+ "address=?,phone=?, where email=?";
	private static final String delete_customer = "delete from customer where email=?";
	private static final String insert_login = "insert into login_credentials (?,?)";
	private static final String ROW_COUNT = "select count(*) from employee_record";
	
	private static final String login_query = "select email,password from login_credentials where email=?";
	//private String lastInsertId;
	private Connection con = DbConnect.getDatabaseConeection();

	/*
	 * public static Connection getDatabaseConeection() { Connection
	 * connection=null;
	 * 
	 * 
	 * try {
	 * 
	 * Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().
	 * newInstance();
	 * connection=DriverManager.getConnection(dbUrl,dbPassword,dbPassword);
	 * 
	 * } catch (InstantiationException | IllegalAccessException |
	 * IllegalArgumentException | InvocationTargetException | NoSuchMethodException
	 * | SecurityException | ClassNotFoundException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } catch (SQLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * 
	 * return connection; }
	 */
	private void addCustomer(RegisterDetail customer, String email) throws Exception {

		try {

			// Connection con=getDatabaseConeection();

			PreparedStatement insertStatement;
			insertStatement = con.prepareStatement(register_customer);
			insertStatement.setString(1, customer.getFirstName());
			insertStatement.setString(2, customer.getLastName());
			insertStatement.setString(3, customer.getAddress());
			insertStatement.setLong(4, customer.getPhoneNumber());
			insertStatement.setString(5, customer.getEmail());
			insertStatement.setString(6, customer.getImage());
			
			
			insertStatement.executeUpdate();

			insertStatement = con.prepareStatement(insert_login);
			insertStatement.setString(1, customer.getEmail());
			insertStatement.setString(2, customer.getPassword());
			String password = PasswordEncryption.encrypt(customer.getEmail(), customer.getPassword());
			customer.setPassword(password);
			insertStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*public void insertEmployeeToDatabase(RegisterDetail customer) throws Exception {

		try {
			// Connection con1=getDatabaseConeection();
			// PreparedStatement rowCountStatement=con1.prepareStatement(ROW_COUNT);
			PreparedStatement rowCountStatement = con.prepareStatement(ROW_COUNT);
			ResultSet rowCountResultsSet = rowCountStatement.executeQuery();
			rowCountResultsSet.next();
			int rowCountValue = rowCountResultsSet.getInt(1);
			if (rowCountValue == 0) {

				this.addCustomer(customer, customer.getId());

			} else {

				/* String lastInsertId=employee.getId();
				PreparedStatement st = con.prepareStatement(
						"select email from customer where email=(select max(Lon) from employee_record)");

				ResultSet res = st.executeQuery();
				while (res.next()) {
					this.lastInsertId = res.getString(1);
					employee.setId(lastInsertId);

				}

				String partialString = this.lastInsertId.substring(0, 7);
				int x = Integer.valueOf(this.lastInsertId.substring(7, 10));

				x = x + 1;

				String updatedlondon_metId = partialString + x;

				this.addEmployee(employee, updatedlondon_metId);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getLocalizedMessage());

		}

	}*/

	private static String decrypt(String encryptedPassword, String email) throws Exception {

		byte[] decode = Base64.getDecoder().decode(encryptedPassword.getBytes(UTF_8));

		// get back the iv and salt from the cipher text
		ByteBuffer bb = ByteBuffer.wrap(decode);

		byte[] iv = new byte[IV_LENGTH_BYTE];
		bb.get(iv);

		byte[] salt = new byte[SALT_LENGTH_BYTE];
		bb.get(salt);

		byte[] cipherText = new byte[bb.remaining()];
		bb.get(cipherText);

		// get back the aes key from the same password and salt
		SecretKey aesKeyFromPassword = PasswordEncryption.getAESKeyFromPassword(email.toCharArray(), salt);

		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

		cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

		byte[] plainText = cipher.doFinal(cipherText);

		return new String(plainText, UTF_8);

	}

	public String getLogin(String email, String password) throws Exception {
		String value = "";
		try {
			PreparedStatement statement = con.prepareStatement(login_query);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				value = result.getString(1);
				String decPassword = decrypt(result.getString("password"), value);
				if (decPassword.equals(password)) {
					return value;
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;

	}

	public List<RegisterDetail> getAllUsers() {
		List<RegisterDetail> customerList = new ArrayList<>();
		try {
			// Connection con=getDatabaseConeection();
			PreparedStatement statement = con.prepareStatement(view_all_customer);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				RegisterDetail customer = new RegisterDetail();
				customer.setFirstName(rs.getString(1));
				customer.setLastName(rs.getString(2));
				customer.setAddress(rs.getString(3));
				customer.setPhoneNumber(rs.getLong(4));
				customer.setEmail(rs.getString(5));
				customer.setPassword(rs.getString(6));
				customer.setImage(rs.getString(7));
				customerList.add(customer);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerList;
	}

	public RegisterDetail getUserById(String email) {
		RegisterDetail customer = new RegisterDetail();
		try {
			// Connection connection=getDatabaseConeection();
//PreparedStatement preparedStatement = connection.prepareStatement("select * from employee_record where London_MetId=?");
			PreparedStatement preparedStatement = con
		.prepareStatement("select * from customer where email=?");
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				customer.setFirstName(rs.getString(1));
				customer.setLastName(rs.getString(2));
				customer.setAddress(rs.getString(3));
				customer.setPhoneNumber(rs.getLong(4));
				customer.setEmail(rs.getString(5));
				customer.setImage(rs.getString(7));
				customer.setPassword(rs.getString(8));
				
				
				customer.setEmail(rs.getString(8));
				customer.setPassword(rs.getString(9));
				customer.setImage(rs.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}

	public void updateEmployee(RegisterDetail customer) {
		// Connection con=getDatabaseConeection();
		// Connection con=getDatabaseConeection();
		try {
			PreparedStatement st = con.prepareStatement(update_customer);
			st.setString(1, customer.getFirstName());
			st.setString(2, customer.getLastName());
			st.setString(3, customer.getAddress());
			st.setLong(4, customer.getPhoneNumber());
			st.setString(5, customer.getEmail());
			st.setString(6, customer.getPassword());
			st.setString(7, customer.getImage());
			st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteById(String email) {
		// Connection con=getDatabaseConeection();
		try {
PreparedStatement deleteStatement = con.prepareStatement(delete_customer);
			deleteStatement.setString(1, email);
			deleteStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

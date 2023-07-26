package project.model;
public class RegisterDetail {
	private String firstName;
	private String lastName;
	private String address;
	private long phoneNumber;
	private String email;
	private String password;
	private String image;

	public void setId(String email) {
		this.email = email;
	}

	public String getId() {
		return this.email;
	}

	public RegisterDetail() {
	}

	public RegisterDetail(String firstName, String lastName, String address, long phoneNumber,
			String email, String password, String image) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.email = email;
		this.password = password;
		this.image = image;

	}



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Employee_Form [getFirstName()=" + getFirstName() + ",getlastName()="+ getLastName()+ ", getPhoneNumber()="
				+ getPhoneNumber() + ", getAddress()=" + getAddress() + "getEmail()=" + getEmail() + ", getPassword()=" + getPassword() + 
				", getImage()=" + getImage() + "]";
	}

}

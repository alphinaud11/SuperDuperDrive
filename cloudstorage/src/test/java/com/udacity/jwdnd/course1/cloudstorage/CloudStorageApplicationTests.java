package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void unauthorizedUser() {
		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		Assertions.assertEquals("Sign Up", driver.getTitle());

		// Visit the login page.
		driver.get("http://localhost:" + this.port + "/login");
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals("Login", driver.getTitle());

		// Visit the home page.
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void loginLogout() {
		// Create a test account and login
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// verify home page is accessible
		Assertions.assertEquals("Home", driver.getTitle());
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// logout
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
		WebElement buttonLogout = driver.findElement(By.id("logout"));
		buttonLogout.click();

		// verify home page is not accessible
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals("Login", driver.getTitle());
	}

	public void createNote(String noteTitle, String noteDescription) {
		// Create a test account and login
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// click notes tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement buttonNotesTab = driver.findElement(By.id("nav-notes-tab"));
		buttonNotesTab.click();

		// show note modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showNoteModal")));
		WebElement buttonshowNoteModal = driver.findElement(By.id("showNoteModal"));
		buttonshowNoteModal.click();

		// fill out note inputs
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		noteTitleInput.click();
		noteTitleInput.sendKeys(noteTitle);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescriptionInput = driver.findElement(By.id("note-description"));
		noteDescriptionInput.click();
		noteDescriptionInput.sendKeys(noteDescription);

		// create note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveNote")));
		WebElement noteSubmit = driver.findElement(By.id("saveNote"));
		noteSubmit.click();

		// go from result page back to home page
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// click notes tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement buttonNotesTab2 = driver.findElement(By.id("nav-notes-tab"));
		buttonNotesTab2.click();

		// wait for new note to load
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(noteTitle+"1")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(noteDescription+"1")));
	}

	@Test
	public void verifyCreateNote() {
		// create new note
		createNote("title", "description");

		// verify note is displayed
		Assertions.assertEquals(driver.findElement(By.id("title1")).getText(), "title");
		Assertions.assertEquals(driver.findElement(By.id("description1")).getText(), "description");
	}

	@Test
	public void verifyEditNote() {
		// create new note
		createNote("title1", "description1");

		// show note modal
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editNote1")));
		WebElement buttonEditNote = driver.findElement(By.id("editNote1"));
		buttonEditNote.click();

		// fill out note inputs
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		noteTitleInput.click();
		noteTitleInput.clear();
		noteTitleInput.sendKeys("title2");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescriptionInput = driver.findElement(By.id("note-description"));
		noteDescriptionInput.click();
		noteDescriptionInput.clear();
		noteDescriptionInput.sendKeys("description2");

		// edit note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveNote")));
		WebElement noteSubmit = driver.findElement(By.id("saveNote"));
		noteSubmit.click();

		// go from result page back to home page
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// click notes tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement buttonNotesTab2 = driver.findElement(By.id("nav-notes-tab"));
		buttonNotesTab2.click();

		// wait for newly edited note to load
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title21")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("description21")));

		// verify note is displayed
		Assertions.assertEquals(driver.findElement(By.id("title21")).getText(), "title2");
		Assertions.assertEquals(driver.findElement(By.id("description21")).getText(), "description2");
	}

	@Test
	public void verifyDeleteNote() {
		// create new note
		createNote("title", "description");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// delete note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote1")));
		WebElement buttonDeleteNote = driver.findElement(By.id("deleteNote1"));
		buttonDeleteNote.click();

		// go from result page back to home page
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// click notes tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement buttonNotesTab2 = driver.findElement(By.id("nav-notes-tab"));
		buttonNotesTab2.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showNoteModal")));

		// verify note is removed
		Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("title1")));
		Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("description1")));
	}

	public void createCredential(String url, String username, String password) {
		// Create a test account and login
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// click credentials tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement buttonCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		buttonCredentialsTab.click();

		// show credentials modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showCredentialModal")));
		WebElement buttonshowCredentialModal = driver.findElement(By.id("showCredentialModal"));
		buttonshowCredentialModal.click();

		// fill out credential inputs
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrlInput = driver.findElement(By.id("credential-url"));
		credentialUrlInput.click();
		credentialUrlInput.sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsernameInput = driver.findElement(By.id("credential-username"));
		credentialUsernameInput.click();
		credentialUsernameInput.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPasswordInput = driver.findElement(By.id("credential-password"));
		credentialPasswordInput.click();
		credentialPasswordInput.sendKeys(password);

		// create credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveCredential")));
		WebElement credentialSubmit = driver.findElement(By.id("saveCredential"));
		credentialSubmit.click();

		// go from result page back to home page
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// click credentials tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement buttonCredentialsTab2 = driver.findElement(By.id("nav-credentials-tab"));
		buttonCredentialsTab2.click();

		// wait for new credential to load
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(url+"1")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(username+"1")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(password+"1")));
	}

	@Test
	public void verifyCreateCredential() {
		// create new credential
		createCredential("youtube.com", "username", "password");

		// verify credential is displayed
		Assertions.assertEquals(driver.findElement(By.id("youtube.com1")).getText(), "youtube.com");
		Assertions.assertEquals(driver.findElement(By.id("username1")).getText(), "username");
		Assertions.assertNotEquals(driver.findElement(By.id("password1")).getText(), "password");
	}

	@Test
	public void verifyEditCredential() {
		// create new credential
		createCredential("youtube.com", "username1", "password1");

		// show credential modal
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCredential1")));
		WebElement buttonEditCredential = driver.findElement(By.id("editCredential1"));
		buttonEditCredential.click();

		// verify password is unencrypted
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPasswordInput = driver.findElement(By.id("credential-password"));
		Assertions.assertEquals(credentialPasswordInput.getAttribute("value"), "password1");

		// fill out credential inputs
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrlInput = driver.findElement(By.id("credential-url"));
		credentialUrlInput.click();
		credentialUrlInput.clear();
		credentialUrlInput.sendKeys("facebook.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsernameInput = driver.findElement(By.id("credential-username"));
		credentialUsernameInput.click();
		credentialUsernameInput.clear();
		credentialUsernameInput.sendKeys("username2");

		credentialPasswordInput.click();
		credentialPasswordInput.clear();
		credentialPasswordInput.sendKeys("password2");

		// edit credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveCredential")));
		WebElement credentialSubmit = driver.findElement(By.id("saveCredential"));
		credentialSubmit.click();

		// go from result page back to home page
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// click credentials tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement buttonCredentialsTab2 = driver.findElement(By.id("nav-credentials-tab"));
		buttonCredentialsTab2.click();

		// show credential modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCredential1")));
		WebElement buttonEditCredential2 = driver.findElement(By.id("editCredential1"));
		buttonEditCredential2.click();

		// wait for input fields to display
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));

		// verify credential changes
		Assertions.assertEquals(driver.findElement(By.id("credential-url")).getAttribute("value"), "facebook.com");
		Assertions.assertEquals(driver.findElement(By.id("credential-username")).getAttribute("value"), "username2");
		Assertions.assertEquals(driver.findElement(By.id("credential-password")).getAttribute("value"), "password2");
	}

	@Test
	public void verifyDeleteCredential() {
		// create new credential
		createCredential("youtube.com", "username", "password");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// delete credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCredential1")));
		WebElement buttonDeleteCredential = driver.findElement(By.id("deleteCredential1"));
		buttonDeleteCredential.click();

		// go from result page back to home page
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// click credentials tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement buttonCredentialsTab2 = driver.findElement(By.id("nav-credentials-tab"));
		buttonCredentialsTab2.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showCredentialModal")));

		// verify credential is removed
		Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("youtube.com1")));
		Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("username1")));
		Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("password1")));
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}

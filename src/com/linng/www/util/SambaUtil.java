package com.linng.www.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbSession;

/**
 * 1.远程Windows 2.远程Linux samba访问文件
 * 
 * @author LiNing
 * 
 */
public class SambaUtil {

	public static final String REMOTE_ADDRESS = "172.16.0.26";

	public static final String USER = "root";
	public static final String PWD = "lining!@#";
	
	private static NtlmPasswordAuthentication auth;

	public static final String SHARE_DOC_NAME = ""; //
	
	static {
		System.out.println("fdas");
		doLogon();
	}
	
	public  SambaUtil () {
		System.out.println("dododod");
	}

	private static void doLogon() {

		InetAddress inetAddress;
		try {
			inetAddress = InetAddress
					.getByName("172.16.10.38");
			UniAddress uniAddress = new UniAddress(inetAddress);
			auth = new NtlmPasswordAuthentication(
					"172.16.10.38",
					"liuzhen",
					"000000");

			SmbSession.logon(uniAddress, auth);

		} catch (UnknownHostException | SmbException e) {
			e.printStackTrace();
		}

	}

	public void liuZhen() {
		
		System.out.println(auth);

		long start = System.currentTimeMillis();
		try {
			String url = "smb://172.16.10.38/share/";
			SmbFile smbFile = new SmbFile(url, auth);

			if (smbFile.exists()) {
				for (SmbFile file : smbFile.listFiles()) {
					System.out.println(file.getName());
				}
			}

			System.out.println("耗时 ： " + (System.currentTimeMillis() - start)
					+ "ms");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("耗时：" + (System.currentTimeMillis() - start));
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		SambaUtil sambaUtil = new SambaUtil();

		sambaUtil.liuZhen();
	}

	public void demo() throws Exception {
		String ipStr = "172.16.10.38"; // 刘振

		String url = "smb://172.16.0.26/samba/";

		InetAddress ip = InetAddress.getByName("172.16.0.26");
		UniAddress uniAddress = new UniAddress(ip);
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(
				"172.16.0.26", "samba", "199151");
		SmbSession.logon(uniAddress, authentication);// 单点登录 ？

		SmbFile smbFile = new SmbFile(url, authentication);

		long start = System.currentTimeMillis();
		if (smbFile.exists()) {

			for (SmbFile file : smbFile.listFiles()) {

				System.out.println(file.getName());

			}

			/*
			 * for (SmbFile file : smbFile.listFiles()) {
			 * System.out.println(file.getName());
			 * System.out.println(file.canWrite());
			 * 
			 * BufferedReader reader = null;
			 * 
			 * reader = new BufferedReader(new InputStreamReader(new
			 * SmbFileInputStream(file)));
			 * 
			 * String line ;
			 * 
			 * line = reader.readLine();
			 * 
			 * while (line != null) { System.out.println(line ); line =
			 * reader.readLine(); }
			 * 
			 * FileChannel channel = null; InputStream input =
			 * file.getInputStream();
			 * 
			 * byte[] buffer = new byte[1024];
			 * 
			 * int len = 0 ; while ((len = input.read(buffer)) != -1 ) {
			 * System.out.write(buffer , 0 , len); }
			 * 
			 * file.
			 * 
			 * 
			 * }
			 */
		}

		System.out.println("耗时 ： " + (System.currentTimeMillis() - start)
				+ "ms");
	}

}

public class Test {
	public static void main(String[] args) {
		String s1 = "{{ name }}";
		String s2 = "<title name>{{ name }}</title>";
		if (s2.contains(s1)) {
			System.out.println("s2包含了s1");
			s2 = s2.replace(s2.substring(s2.indexOf(s1), s2.indexOf(s1) + s1.length()), "222");
			System.out.println(s2);
		} else {
			System.out.println("s2不包含s1");
		}
	}
}

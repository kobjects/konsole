import SwiftUI
import shared
import demo

struct ContentView: View {
	let greet = "HelloWorld"

	var body: some View {
		    Text(Demo.Companion.shared.ALL[0].name)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
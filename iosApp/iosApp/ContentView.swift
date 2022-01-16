import SwiftUI
import shared
import demo

struct ContentView: View {
	let greet = "HelloWorld"

	var body: some View {
	    NavigationView {
	        List(Demo.Companion.shared.ALL, id: \.name) { demo in
	            NavigationLink {
    		        DemoView(demo: demo)
	    	    } label: {
	    	        Text(demo.name)
	    	    }
		    }
            .navigationTitle("KonsoleDemo")
		}
	}
}

struct MenuView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

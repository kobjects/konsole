import SwiftUI
import demo

struct DemoView: View {
    var demo: Demo
    @State var output: [Output] = []
    @State var requests: [InputRequest] = []
    @State private var input: String = ""

    let konsole = KonsoleImpl()

    func write(text: String) {
        output.append(Output(text: text, right: false))
    }

    func read(consumer: @escaping (String) -> KotlinUnit) {
        requests.append(InputRequest(consumer: consumer))
    }

    var body: some View {
        NavigationView {
            VStack {
                List (output) { line in
                    HStack {
                        if (line.right) {
                            Spacer()
                        }
                        Text(line.text)
                    }
                }
                HStack {
                    let disabled = requests.isEmpty
                    let request = disabled ? nil : requests[0]
                    TextField("", text: $input)
                    .disabled(disabled)
                    Button("Enter", action: {
                        if (request != nil) {
                            requests.remove(at: 0)
                            output.append(Output(text: input, right: true))
                            request?.consumer(input)
                        }
                        input = ""
                    }
                    )
                    .disabled(disabled)
                }
            }
        }
        .navigationTitle(demo.name)
        .onAppear{
            konsole.writeFunction = write
            konsole.readFunction = read
            demo.run(konsole: konsole) {  /*result,*/ error in
                output.append(Output(text: "(terminated)", right: false))
            }
        }
    }


    struct Output: Identifiable {
        let id = UUID()
        let text: String
        let right: Bool
    }

    struct InputRequest {
        let consumer: (String) -> KotlinUnit
    }

}

struct DemoView_Previews: PreviewProvider {
    static var previews: some View {
        DemoView(demo: Demo.Companion.shared.ALL[0])
    }
}

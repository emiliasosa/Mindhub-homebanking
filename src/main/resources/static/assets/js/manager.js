const {createApp} = Vue


createApp({
    data(){
        return{
            id: "",
            clients: [],
            json: "",
            firstName: "",
            lastName: "",
            email: "",
            aux: "",
            lastNameUpdate: "",
            firstNameUpdate: "",
            emailUpdate: ""
        }
   },
   created(){
        this.loadData()
        this.aux = false
       
   },
   methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients")
            .then(res => {
                this.clients = res.data
                this.json = res.data
            })
            
        },
        addClient(){
            
            if(this.firstName != "" && this.lastName != "" && this.email != ""){
                this.postClient()
                location.reload()
            }else{
 
                document.getElementById("text-error").innerText = "All fields are required"
            }
        },
        postClient(){
            axios.post('http://localhost:8080/api/clients', {
                firstName: this.firstName,
                lastName: this.lastName, 
                email: this.email
            })
            .then(res=> {
                this.loadData()
            });
        },
        deleteClient(id){
            axios.delete(id)
            .then(() => this.loadData());
        },
        clickUpdate(id, name){
            this.aux=true
            this.id = id
            this.firstNameUpdate = name
        },
        updateClient(id, firstName, lastName, email){
            axios.put(this.id, {
                firstName: firstName,
                lastName: lastName, 
                email: email
            })
            .then(res=> {
                this.loadData()
            });

            this.aux=false
        }
    }
}).mount("#app")
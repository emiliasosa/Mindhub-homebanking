const {createApp} = Vue


createApp({
    data(){
        return{
            id: "",
            number: "",
            transactions:"",
            isDebit: false,
            allTransactions: [],
            transactions: [],
            num1: 0,
            num2: 5,
            auxNext: false,
            auxPre: false,
            switchDescAsc: true,
            asce: [],
            auxShowIconTableDescription: true,
            auxShowIconTableType: true,
            auxShowIconTableDate: true,
            auxShowIconTableAmount: true,
            nextAccount: undefined,
            account: [],
            firstName: "",
        }
   },
   created(){
    this.paramLocation = location.search
    this.params = new URLSearchParams(this.paramLocation)
    this.id = this.params.get("id")
    this.loadData()
   },
   methods:{
        loadData(){
            axios.get(`/api/clients/current/accounts/${this.id}`)
            .then(res => {
                this.number = res.data.number
                this.balance = res.data.balance.toLocaleString('en-US', {style:"currency", currency:"USD"})
                this.allTransactions = res.data.transactions
                this.transactions = this.allTransactions.sort((a, b) => b.id - a.id).slice(this.num1, this.num2);
                this.fillTable()
            })
            .catch((error)=>{console.log(error)})
        },
        fillTable(){
            if(this.transactions.length < 5 && this.transactions.length >= 0){
                for(let i = this.transactions.length; i < 5; i++){
                        this.transactions.push({description: "-", type: "-", amount: "-", date: "-"})
                }
                this.auxNext = true
            }
        },
        changeOrderAscendent(propertyToChangeOrder){
            if(propertyToChangeOrder == 'description'){
                this.asce = this.allTransactions.sort((c, d)=> c.description.toLowerCase().charCodeAt(0) - d.description.toLowerCase().charCodeAt(0)).slice(this.num1, this.num2);
                this.transactions = this.asce
                this.auxShowIconTableDescription = false    
                this.fillTable()
            }else if(propertyToChangeOrder == 'date'){
                this.asce = this.allTransactions.sort((a, b)=> {
                    if(a.date < b.date){ 
                        return -1; 
                    }else if(a.date > b.date){ 
                        return 1; 
                    }else{
                        return 0
                    }
                }).slice(this.num1, this.num2);
                this.transactions = this.asce
                this.auxShowIconTableDate = false
                this.fillTable()
            }else if(propertyToChangeOrder == 'amount'){
                this.asce = this.allTransactions.sort((a, b)=> {
                    if(a.amount < b.amount){ 
                        return -1; 
                    }else if(a.amount > b.amount){ 
                        return 1; 
                    }else{
                        return 0
                    }
                }).slice(this.num1, this.num2);
                this.transactions = this.asce
                this.auxShowIconTableAmount = false
                this.fillTable()
            }     
        },
        changeOrderDescendent(propertyToChangeOrder){
            if(propertyToChangeOrder == 'description'){
                this.asce = this.allTransactions.sort((c, d)=> d.description.toLowerCase().charCodeAt(0) - c.description.toLowerCase().charCodeAt(0)).slice(this.num1, this.num2);
                this.transactions = this.asce
                this.auxShowIconTableDescription = true
                this.fillTable()
            }else if(propertyToChangeOrder == 'date'){
                this.asce = this.allTransactions.sort((a, b)=> {
                    if(a.date > b.date){ 
                        return -1; 
                    }else if(a.date < b.date){ 
                        return 1; 
                    }else{
                        return 0
                    }
                }).slice(this.num1, this.num2);
                this.transactions = this.asce
                this.auxShowIconTableDate = true
                this.fillTable()
            }else if(propertyToChangeOrder == 'amount'){
                this.asce = this.allTransactions.sort((a, b)=> {
                    if(a.amount > b.amount){ 
                        return -1; 
                    }else if(a.amount < b.amount){ 
                        return 1; 
                    }else{
                        return 0
                    }
                }).slice(this.num1, this.num2);
                this.transactions = this.asce
                this.auxShowIconTableAmount = true
                this.fillTable()
            }
        },
        addTransactions(){
            this.auxNext = false
            this.auxPre = true
            this.num1 += 5
            this.num2 += 5
            this.transactions = this.allTransactions.slice(this.num1, this.num2); 
            this.fillTable()
        },
        discountTransactions(){
            this.auxNext = false
            this.auxPre = true
            this.num1 -= 5
            this.num2 -= 5
            this.transactions = this.allTransactions.slice(this.num1, this.num2); 
            if(this.num1 === 0){
                this.auxPre = false
            }
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "./index.html")
        }
        
        
    }
}).mount("#app")
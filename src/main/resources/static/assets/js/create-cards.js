const {createApp} = Vue

createApp({
    data(){
        return{
            id: "",
            firstName: "",
            lastName: "",
            picture: "",
            type: "",
            color:"",
            creditCards: [],
            debitCards: [],
            allColors: ['GOLD', 'SILVER', 'TITANIUM']
        }
   },
   created(){
    
    this.loadData()
   },
   methods:{
        loadData(){
            axios.get(`/api/clients/current`)
            .then(res => {
                this.firstName = res.data.firstName
                this.lastName = res.data.lastName
                this.cards = res.data.cards
                this.creditCards = this.cards.sort((a, b) => a.id - b.id).filter(card => card.type == 'CREDIT')
                this.debitCards = this.cards.sort((a, b) => a.id - b.id).filter(card => card.type == 'DEBIT');
                if(this.debitCards.length === 3){
                    let disable = document.getElementById('debitType')
                    disable.setAttribute("disabled", "")
                }else if(this.creditCards.length === 3){
                    let disable = document.getElementById('creditType')
                    disable.setAttribute("disabled", "")
                }
            })  
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "./index.html")
        },
        createCard(){
            if(this.type && this.color){
                axios.post('/api/clients/current/cards',`type=${this.type}&color=${this.color}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(res => {
                    this.hiddenAlert()
                    let alertTxt = document.getElementById('alertInnerMessage')
                    alertTxt.innerHTML = `
                        <lottie-player src="https://assets1.lottiefiles.com/packages/lf20_51yyasis.json"  background="transparent"  class="img__lottieAlert" speed=".5" autoplay></lottie-player>
                        <p>You created a new card</p>
                        `
                    setTimeout(()=>{
                    window.location.href = "./cards.html"
                    }, 3000);
                })
                .catch(err => {
                    this.hiddenAlert()
                    let alertTxt = document.getElementById('alertInnerMessage')
                    alertTxt.innerHTML = `
                        <lottie-player src="https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json"  background="transparent"  speed=".5"  class="img__lottieAlert"   loop autoplay></lottie-player>
                        <p>${err.response.data}</p>
                        `
                })
            }else{
                this.hiddenAlert()
                let alertTxt = document.getElementById('alertInnerMessage')
                if(this.type == ""){
                    alertTxt.innerHTML = `
                    <lottie-player src="https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json"  background="transparent"  speed=".5"  class="img__lottieAlert"   loop autoplay></lottie-player>
                    <p>You need to select a <b>type</b> of card.</p>
                    `
                }else{
                    alertTxt.innerHTML = `
                    <lottie-player src="https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json"  background="transparent"  speed=".5"  class="img__lottieAlert"   loop autoplay></lottie-player>
                    <p>You need to select a <b>color</b> of card.</p>
                    `
                }
            }
        },
        active(idToActive, idToDesactive, idToDesactiveSecond, typeToCompare){
            let active = document.getElementById(idToActive)
            let desactive = document.getElementById(idToDesactive)
            let desactiveSecond = document.getElementById(idToDesactiveSecond)

            if(active.classList.length == 1){
                active.classList.toggle('benefits__active')
            }

            if(desactive.classList.length > 1){
                desactive.classList.toggle('benefits__active') 
            }
            if(idToDesactiveSecond){
                if(desactiveSecond.classList.length > 1){
                    desactiveSecond.classList.toggle('benefits__active')
                }
            }

            this.verifyCard(typeToCompare)
        },
        verifyCard(typeToCompare){
            if(typeToCompare){
                for(card of this.cards){
                    let disable = document.getElementById(card.color)
                    disable.removeAttribute("disabled")
                    disable.checked = false
                    this.color = ""
                    for(color of this.allColors){
                        let active = document.getElementById(`${color}__benefit`)
                        active.classList.remove('benefits__active')
                    }
                }
                if(typeToCompare === "credit"){
                    for(card of this.creditCards){
                        let disable = document.getElementById(card.color)
                        disable.setAttribute("disabled", "")
                        let active = document.getElementById(`${card.color}__benefit`)
                        if(active.classList.length > 1){
                            active.classList.toggle('benefits__active')
                        }
                    }
                }else if(typeToCompare === "debit"){
                    for(card of this.debitCards){
                        let disable = document.getElementById(card.color)
                        disable.setAttribute("disabled", "")
                        let active = document.getElementById(`${card.color}__benefit`)
                        if(active.classList.length > 1){
                            active.classList.toggle('benefits__active')
                        }
                    }
                }
            }   
        },
        hiddenAlert(){
            document.getElementById("alertCreatedCard").classList.toggle("hidden__container")
        }
    }
}).mount("#app")
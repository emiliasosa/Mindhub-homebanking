const {createApp} = Vue

createApp({
    data(){
        return{
            cards: [],
            creditCards: [],
            debitCards: [],
            firstName: "",
            lastName: "",
            picture: "",
            moveXDebit: 0,
            moveXCredit: 0,
            auxSlider: 0,
            auxSliderCredit: 0,
            auxSliderDebit: 0,
            creditActualCard: 1,
            debitActualCard: 1,
            date: ""
        }
    },
    created(){
        let date = new Date()
        let month = date.getMonth() +1
        this.date = date.getFullYear() +"-0"+month+"-"+date.getDate()
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get(`http://localhost:8080/api/clients/current`)
            .then(res => {
                this.cards = res.data.cards
                this.creditCards = this.cards.sort((a, b) => a.id - b.id).filter(card => card.type == 'CREDIT')
                this.debitCards = this.cards.sort((a, b) => a.id - b.id).filter(card => card.type == 'DEBIT');
                this.expireCards = this.cards.filter(card => card.thruDate < this.date).map(card => card.number)
                this.firstName = res.data.firstName
                this.lastName = res.data.lastName
                this.picture = res.data.picture
            })
            .catch((error)=>{console.log(error)})
        },
        sliderCreditDebit(placeToMove, idContainer, id, comparationCards){
            const sliderLeft = document.getElementById(idContainer)
            if(placeToMove === 'left' && this.auxSliderDebit > 0 && comparationCards === 'debit'){
                this.moveXDebit -= document.getElementById(id).offsetWidth
                sliderLeft.style.transform = `translateX(-${this.moveXDebit}px)`;
                this.auxSliderDebit -= 1
                if(this.debitActualCard > 1){
                    this.debitActualCard -= 1
                }
            }else if(placeToMove === 'left' && this.auxSliderCredit > 0  && comparationCards === 'credit'){
                this.moveXCredit -= document.getElementById(id).offsetWidth
                sliderLeft.style.transform = `translateX(-${this.moveXCredit}px)`;
                this.auxSliderCredit -= 1
                if(this.creditActualCard > 1){
                    this.creditActualCard -= 1
                }
            }else if(placeToMove === 'rigth' && this.auxSliderDebit < this.debitCards.length -1 && comparationCards === 'debit'){
                this.moveXDebit += document.getElementById(id).offsetWidth 
                sliderLeft.style.transform = `translateX(-${this.moveXDebit}px)`;
                this.auxSliderDebit += 1
                if(this.debitActualCard >= 1 && this.debitActualCard < 4 ){
                    this.debitActualCard += 1
                }
            } else if(placeToMove === 'rigth' && this.auxSliderCredit < this.creditCards.length -1 && comparationCards === 'credit'){
                this.moveXCredit += document.getElementById(id).offsetWidth 
                sliderLeft.style.transform = `translateX(-${this.moveXCredit}px)`;
                this.auxSliderCredit += 1
                if(this.creditActualCard >= 1 && this.creditActualCard < 4 ){
                    this.creditActualCard += 1
                }
            }
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "./index.html")
                       
        },
        hiddenAlert(id){
            document.getElementById(id).classList.toggle("hidden__container")
        },
        textImageModalQuestion(image, message, id, loop, margin){
            let alertTxt = document.getElementById(id)
            alertTxt.innerHTML = `
                    <lottie-player src="${image}"  background="transparent"  class="img__lottieAlert" ${loop} speed=".8" autoplay></lottie-player>
                    <p class="text-center ${margin}">${message}</p>
                `
        },
        reload(){
            setTimeout(()=>{
                location. reload()
            }, 4000);
        },
        showQuestion(action, number){
            if(number){
                this.number = number
            }
            document.getElementById("questionYesOrNo").classList.remove("hidden__container")
            this.hiddenAlert("alertCreatedCard")
            if(action === "add"){
                this.textImageModalQuestion("https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json", "Are you sure to add another account?", 'alertInnerLottie')
                this.action = "addPrevious"
            }else{
                this.textImageModalQuestion("https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json", "Are you sure to delete this card?", 'alertInnerLottie')
                this.action = "delete"
            }            
        },
        questionCreatedCardResponse(response){
            this.questionCreatedCard = response
            if(this.action === "delete"){
                this.delete()
            }if(this.action === "add"){
                this.addAccount()
            }           
        },
        delete(){            
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true'){
                    axios.patch(`/api/clients/current/card/delete`,`disable=true&number=${this.number}`,
                    {headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(res=> {
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                        this.textImageModalQuestion('https://assets1.lottiefiles.com/packages/lf20_51yyasis.json', res.data, 'alertInnerLottie')  
                        this.creditCards = this.cards.sort((a, b) => a.id - b.id).filter(card => card.type == 'CREDIT' && card.number != this.number)
                        this.debitCards = this.cards.sort((a, b) => a.id - b.id).filter(card => card.type == 'DEBIT' && card.number != this.number);                  
                    })
                    .catch(err => {
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                        this.textImageModalQuestion('https://assets2.lottiefiles.com/temp/lf20_QYm9j9.json',err.response.data, 'alertInnerLottie')
                    })
                }else if(this.questionCreatedCard === 'false'){
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.textImageModalQuestion("https://assets2.lottiefiles.com/temp/lf20_QYm9j9.json", "You don't delete the card.", 'alertInnerLottie')   
                }
            }, 500);
        },
        claimedCard(number){
            this.hiddenAlert("alertCreatedCard")
            document.getElementById("questionYesOrNo").classList.add("hidden__container")
            this.textImageModalQuestion("https://assets6.lottiefiles.com/packages/lf20_b5tId6.json", `Our system has already registered that the card number<b> ${number}</b>
            has not yet arrived. In the next 5 business days you will receive your card at your address. ${this.firstName} thank you for using our services.`, 'alertInnerLottie', 'loop', 'mt-4')
        },
        renewCard(number){
            this.hiddenAlert("alertCreatedCard")
            setTimeout(()=>{
                axios.patch(`/api/clients/current/card/renew`,`numberRenewCard=${number}`,
                {headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(res=> {
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.textImageModalQuestion('https://assets1.lottiefiles.com/packages/lf20_51yyasis.json', res.data, 'alertInnerLottie')                         
                })
                .catch(err => {
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.textImageModalQuestion('https://assets2.lottiefiles.com/temp/lf20_QYm9j9.json',err.response.data, 'alertInnerLottie')
                })
            }, 500);
        }
    }
}).mount("#app")
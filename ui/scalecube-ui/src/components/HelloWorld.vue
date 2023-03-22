<script>
import { createMicroservice, ASYNC_MODEL_TYPES } from '@scalecube/browser';


export default {
  data() {
    return {
      count: 0,
      connection: new WebSocket("wss://localhost:8081")
    }
  },
  methods: {
    increment() {
      this.count++
      this.connection.onopen = function(event) {
        console.log("Opened the connection")
        console.log(event)
      }
      this.connection.onmessage = function(event) {
        console.log(event)
      }  
    }, 
    send() {
      this.connection.send("Hello")
    }
  }
}
</script>

<template>
  <button @click="increment">count is: {{ count }}</button>
  <button @click="send">count is: {{ count }}</button>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>

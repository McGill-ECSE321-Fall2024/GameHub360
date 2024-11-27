"use client"

import React from "react"
import {
  Accordion,
  AccordionItem,
  AccordionTrigger,
  AccordionContent,
} from "./Accordion"
import { PaymentCard } from "../api/customerService"

interface PaymentCardsProps {
  paymentCards: PaymentCard[]
}

const PaymentCards: React.FC<PaymentCardsProps> = ({ paymentCards }) => {
  if (!paymentCards || paymentCards.length === 0) {
    return <p>No payment cards available.</p>
  }

  return (
    <Accordion type="single" collapsible>
      {paymentCards.map((card) => (
        <AccordionItem key={card.paymentDetailsId} value={`card-${card.paymentDetailsId}`}>
          <AccordionTrigger>
            **** **** **** {card.cardNumber.slice(-4)} {/* Display masked card number */}
          </AccordionTrigger>
          <AccordionContent>
            <p><strong>Card Name:</strong> {card.cardName}</p>
            <p><strong>Expiration:</strong> {card.expMonth}/{card.expYear}</p>
            <p><strong>Postal Code:</strong> {card.postalCode}</p>
          </AccordionContent>
        </AccordionItem>
      ))}
    </Accordion>
  )
}

export default PaymentCards

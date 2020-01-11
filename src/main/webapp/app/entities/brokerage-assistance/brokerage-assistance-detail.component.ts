import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';

@Component({
    selector: 'jhi-brokerage-assistance-detail',
    templateUrl: './brokerage-assistance-detail.component.html'
})
export class BrokerageAssistanceDetailComponent implements OnInit {
    brokerageAssistance: IBrokerageAssistance;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerageAssistance }) => {
            this.brokerageAssistance = brokerageAssistance;
        });
    }

    previousState() {
        window.history.back();
    }
}

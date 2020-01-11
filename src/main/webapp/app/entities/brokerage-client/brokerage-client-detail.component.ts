import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBrokerageClient } from 'app/shared/model/brokerage-client.model';

@Component({
    selector: 'jhi-brokerage-client-detail',
    templateUrl: './brokerage-client-detail.component.html'
})
export class BrokerageClientDetailComponent implements OnInit {
    brokerageClient: IBrokerageClient;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerageClient }) => {
            this.brokerageClient = brokerageClient;
        });
    }

    previousState() {
        window.history.back();
    }
}

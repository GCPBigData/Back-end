import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBrokerage } from 'app/shared/model/brokerage.model';

@Component({
    selector: 'jhi-brokerage-detail',
    templateUrl: './brokerage-detail.component.html'
})
export class BrokerageDetailComponent implements OnInit {
    brokerage: IBrokerage;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerage }) => {
            this.brokerage = brokerage;
        });
    }

    previousState() {
        window.history.back();
    }
}

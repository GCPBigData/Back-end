import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStockWatch } from 'app/shared/model/stock-watch.model';

@Component({
    selector: 'jhi-stock-watch-detail',
    templateUrl: './stock-watch-detail.component.html'
})
export class StockWatchDetailComponent implements OnInit {
    stockWatch: IStockWatch;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ stockWatch }) => {
            this.stockWatch = stockWatch;
        });
    }

    previousState() {
        window.history.back();
    }
}
